// sv/edu/ues/pedidosapp/features/carrito/ui/CarritoFragment.java
package sv.edu.ues.pedidosapp.features.carrito.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.features.carrito.viewmodel.CarritoViewModel;
import sv.edu.ues.pedidosapp.features.pedidos.ui.AgregarPedidoDialogFragment;

public class CarritoFragment extends Fragment {

    private CarritoViewModel carritoViewModel;
    private RecyclerView recyclerView;
    private CarritoAdapter carritoAdapter;
    private TextView totalTextView;
    private Button crearPedidoButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carrito, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar ViewModel
        carritoViewModel = new ViewModelProvider(requireActivity()).get(CarritoViewModel.class);

        // Inicializar RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_carrito);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        carritoAdapter = new CarritoAdapter(getContext(), carritoViewModel);
        recyclerView.setAdapter(carritoAdapter);

        // Inicializar TextView para el total
        totalTextView = view.findViewById(R.id.text_view_total);

        // Inicializar Button para crear pedido
        crearPedidoButton = view.findViewById(R.id.button_crear_pedido);
        crearPedidoButton.setOnClickListener(v -> {
            mostrarDialogoCrearPedido();
        });

        // Observar cambios en la lista de productos seleccionados
        carritoViewModel.getProductosSeleccionados().observe(getViewLifecycleOwner(), productos -> {
            carritoAdapter.setProductos(productos);
            carritoAdapter.notifyDataSetChanged();
        });

        // Observar cambios en el total del carrito
        carritoViewModel.getTotalCarrito().observe(getViewLifecycleOwner(), total -> {
            totalTextView.setText(String.format("Total: $%.2f", total));
        });

        FloatingActionButton fabVaciarCarrito = view.findViewById(R.id.fab_vaciar_carrito);
        fabVaciarCarrito.setOnClickListener(v -> {
            carritoViewModel.limpiarCarrito();
            Snackbar.make(view, "Carrito vaciado", Snackbar.LENGTH_SHORT).show();
        });
    }

    private void mostrarDialogoCrearPedido() {
        AgregarPedidoDialogFragment dialog = new AgregarPedidoDialogFragment();
        dialog.show(getChildFragmentManager(), "CrearPedidoDialog");
    }
}