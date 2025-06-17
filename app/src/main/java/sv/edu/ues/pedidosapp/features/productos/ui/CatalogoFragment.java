package sv.edu.ues.pedidosapp.features.productos.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.features.core.ViewModelFactory;
import sv.edu.ues.pedidosapp.features.productos.viewmodel.ProductoViewModel;

public class CatalogoFragment extends Fragment {

    private ProductoViewModel productoViewModel;
    private RecyclerView recyclerView;
    private ProductoAdapter productoAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_catalogo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar ViewModel
        ViewModelFactory factory = new ViewModelFactory(getActivity().getApplication());
        productoViewModel = new ViewModelProvider(this, factory).get(ProductoViewModel.class);

        // Inicializar RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_catalogo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productoAdapter = new ProductoAdapter(getContext());
        recyclerView.setAdapter(productoAdapter);

        // Observar cambios en la lista de productos
        productoViewModel.getProductosDisponibles().observe(getViewLifecycleOwner(), productos -> {
            productoAdapter.setProductos(productos);
        });
    }
}