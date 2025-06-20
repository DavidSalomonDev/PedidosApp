package sv.edu.ues.pedidosapp.features.pedidos.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.data.local.entity.Pedido;
import sv.edu.ues.pedidosapp.features.core.ViewModelFactory;
import sv.edu.ues.pedidosapp.features.pedidos.viewmodel.PedidoViewModel;
import sv.edu.ues.pedidosapp.utils.Constants;

public class ListaPedidosFragment extends Fragment implements PedidoAdapter.OnPedidoClickListener, SearchView.OnQueryTextListener {

    private final List<Pedido> listaPedidosOriginal = new ArrayList<>();
    private PedidoViewModel pedidoViewModel;
    private RecyclerView recyclerView;
    private PedidoAdapter pedidoAdapter;
    private FloatingActionButton fabAgregarPedido;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Habilitar el menú de opciones
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_pedidos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);

        // Inicializar ViewModel
        ViewModelFactory factory = new ViewModelFactory(getActivity().getApplication());
        pedidoViewModel = new ViewModelProvider(this, factory).get(PedidoViewModel.class);

        // Inicializar RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_pedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pedidoAdapter = new PedidoAdapter(getContext(), this);
        recyclerView.setAdapter(pedidoAdapter);

        // Inicializar FloatingActionButton
        fabAgregarPedido = view.findViewById(R.id.fab_agregar_pedido);
        fabAgregarPedido.setOnClickListener(v -> {
            mostrarDialogoAgregarPedido();
        });

        // Obtener ID del usuario logueado
        long userId = sharedPreferences.getLong(Constants.PREF_USER_ID, -1);

        // Observar cambios en la lista de pedidos del usuario
        if (userId != -1) {
            pedidoViewModel.getPedidosByUsuario((int) userId).observe(getViewLifecycleOwner(), pedidos -> {
                listaPedidosOriginal.clear();
                listaPedidosOriginal.addAll(pedidos);
                pedidoAdapter.setPedidos(pedidos);
            });
        } else {
            // Si no hay usuario logueado, mostrar todos los pedidos
            pedidoViewModel.getAllPedidos().observe(getViewLifecycleOwner(), pedidos -> {
                listaPedidosOriginal.clear();
                listaPedidosOriginal.addAll(pedidos);
                pedidoAdapter.setPedidos(pedidos);
            });
        }

        // Observar resultado de operaciones
        pedidoViewModel.getOperationResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_lista_pedidos, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter_pendiente) {
            filtrarPedidosPorEstado(Constants.ESTADO_PENDIENTE);
            return true;
        } else if (id == R.id.action_filter_confirmado) {
            filtrarPedidosPorEstado(Constants.ESTADO_CONFIRMADO);
            return true;
        } else if (id == R.id.action_filter_en_preparacion) {
            filtrarPedidosPorEstado(Constants.ESTADO_EN_PREPARACION);
            return true;
        } else if (id == R.id.action_filter_entregado) {
            filtrarPedidosPorEstado(Constants.ESTADO_ENTREGADO);
            return true;
        } else if (id == R.id.action_filter_cancelado) {
            filtrarPedidosPorEstado(Constants.ESTADO_CANCELADO);
            return true;
        } else if (id == R.id.action_filter_todos) {
            mostrarTodosLosPedidos();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Métodos para filtrar y buscar
    private void filtrarPedidosPorEstado(String estado) {
        List<Pedido> pedidosFiltrados = new ArrayList<>();
        for (Pedido pedido : listaPedidosOriginal) {
            if (pedido.getEstado().equals(estado)) {
                pedidosFiltrados.add(pedido);
            }
        }
        pedidoAdapter.setPedidos(pedidosFiltrados);
    }

    private void mostrarTodosLosPedidos() {
        pedidoAdapter.setPedidos(listaPedidosOriginal);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        buscarPedidos(newText);
        return true;
    }

    private void buscarPedidos(String query) {
        List<Pedido> pedidosFiltrados = new ArrayList<>();
        for (Pedido pedido : listaPedidosOriginal) {
            if (String.valueOf(pedido.getIdPedido()).contains(query) ||
                    pedido.getEstado().toLowerCase().contains(query.toLowerCase())) {
                pedidosFiltrados.add(pedido);
            }
        }
        pedidoAdapter.setPedidos(pedidosFiltrados);
    }

    // Método para mostrar el diálogo de agregar pedido
    private void mostrarDialogoAgregarPedido() {
        AgregarPedidoDialogFragment dialog = new AgregarPedidoDialogFragment();
        dialog.show(getChildFragmentManager(), "AgregarPedidoDialog");
    }


    // Implementar métodos de la interfaz OnPedidoClickListener (solo con long)
    @Override
    public void onPedidoClick(long idPedido) {
        // Mostrar detalles del pedido
        DetallePedidoDialogFragment dialog = DetallePedidoDialogFragment.newInstance(idPedido);
        dialog.show(getChildFragmentManager(), "DetallePedidoDialog");
    }

    @Override
    public void onEditarPedido(long idPedido) {
        // Mostrar diálogo para editar pedido
        EditarPedidoDialogFragment dialog = EditarPedidoDialogFragment.newInstance((int) idPedido);
        dialog.show(getChildFragmentManager(), "EditarPedidoDialog");
    }

    @Override
    public void onEliminarPedido(long idPedido) {
        // Confirmar eliminación
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Eliminar Pedido")
                .setMessage("¿Está seguro de que desea eliminar este pedido?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    pedidoViewModel.deletePedidoById(idPedido);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onCambiarEstado(long idPedido, String nuevoEstado) {
        pedidoViewModel.updateEstadoPedido(idPedido, nuevoEstado);
    }
}