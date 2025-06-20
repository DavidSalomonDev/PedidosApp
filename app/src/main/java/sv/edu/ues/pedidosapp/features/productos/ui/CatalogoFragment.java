// sv/edu/ues/pedidosapp/features/productos/ui/CatalogoFragment.java
package sv.edu.ues.pedidosapp.features.productos.ui;

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
import com.google.android.material.navigation.NavigationView;

import sv.edu.ues.pedidosapp.MainActivity;
import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.data.local.entity.Producto;
import sv.edu.ues.pedidosapp.features.carrito.ui.CarritoFragment;
import sv.edu.ues.pedidosapp.features.carrito.viewmodel.CarritoViewModel;
import sv.edu.ues.pedidosapp.features.core.ViewModelFactory;
import sv.edu.ues.pedidosapp.features.productos.viewmodel.ProductoViewModel;

public class CatalogoFragment extends Fragment implements SearchView.OnQueryTextListener, ProductoAdapter.OnProductoClickListener {

    private ProductoViewModel productoViewModel;
    private CarritoViewModel carritoViewModel;
    private RecyclerView recyclerView;
    private ProductoAdapter productoAdapter;
    //private FloatingActionButton fabAgregarProducto;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_catalogo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar ViewModels
        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
        productoViewModel = new ViewModelProvider(this, factory).get(ProductoViewModel.class);
        carritoViewModel = new ViewModelProvider(requireActivity()).get(CarritoViewModel.class);

        // Inicializar RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_catalogo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productoAdapter = new ProductoAdapter(getContext(), this);
        recyclerView.setAdapter(productoAdapter);

        FloatingActionButton fabIrCarrito = view.findViewById(R.id.fab_ir_carrito);
        fabIrCarrito.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).displayFragment(new CarritoFragment());
                // Opcional: actualiza el ítem seleccionado en el menú lateral
                NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                if (navigationView != null) {
                    navigationView.setCheckedItem(R.id.nav_carrito);
                }
            }
        });

        // Observar cambios en la lista de productos
        productoViewModel.getAllProductosPagingData().observe(getViewLifecycleOwner(), productos -> {
            productoAdapter.submitData(getLifecycle(), productos);
        });

        // Observar resultado de operaciones
        productoViewModel.getOperationResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_catalogo, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.action_agregar_producto) {
//            mostrarDialogoAgregarProducto();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    // Métodos para buscar
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        buscarProductos(newText);
        return true;
    }

    private void buscarProductos(String query) {
        productoViewModel.buscarProductosPorNombrePagingData(query).observe(getViewLifecycleOwner(), productos -> {
            productoAdapter.submitData(getLifecycle(), productos);
        });
    }

//    // Método para mostrar el diálogo de agregar producto
//    private void mostrarDialogoAgregarProducto() {
//        AgregarProductoDialogFragment dialog = new AgregarProductoDialogFragment();
//        dialog.show(getChildFragmentManager(), "AgregarProductoDialog");
//    }

//    // Implementar métodos de la interfaz OnProductoClickListener
//    @Override
//    public void onEditarProducto(int idProducto) {
//        EditarProductoDialogFragment dialog = EditarProductoDialogFragment.newInstance(idProducto);
//        dialog.show(getChildFragmentManager(), "EditarProductoDialog");
//    }

//    @Override
//    public void onEliminarProducto(int idProducto) {
//        new AlertDialog.Builder(getContext())
//                .setTitle("Eliminar Producto")
//                .setMessage("¿Está seguro de que desea eliminar este producto?")
//                .setPositiveButton("Eliminar", (dialog, which) -> {
//                    productoViewModel.deleteProductoById(idProducto);
//                })
//                .setNegativeButton("Cancelar", null)
//                .show();
//    }

    @Override
    public void onAgregarAlCarrito(Producto producto) {
        // Mostrar diálogo para seleccionar cantidad
        mostrarDialogoCantidad(producto);
    }

    private void mostrarDialogoCantidad(Producto producto) {
        SeleccionarCantidadDialogFragment dialog = SeleccionarCantidadDialogFragment.newInstance(producto);
        dialog.show(getChildFragmentManager(), "SeleccionarCantidadDialog");
    }
}