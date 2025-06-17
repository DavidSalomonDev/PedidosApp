package sv.edu.ues.pedidosapp.features.pedidos.ui;

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
import sv.edu.ues.pedidosapp.features.pedidos.viewmodel.PedidoViewModel;
import sv.edu.ues.pedidosapp.features.core.ViewModelFactory;

public class ListaPedidosFragment extends Fragment {

    private PedidoViewModel pedidoViewModel;
    private RecyclerView recyclerView;
    private PedidoAdapter pedidoAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_pedidos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar ViewModel
        ViewModelFactory factory = new ViewModelFactory(getActivity().getApplication());
        pedidoViewModel = new ViewModelProvider(this, factory).get(PedidoViewModel.class);

        // Inicializar RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_pedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pedidoAdapter = new PedidoAdapter(getContext());
        recyclerView.setAdapter(pedidoAdapter);

        // Observar cambios en la lista de pedidos
        pedidoViewModel.getAllPedidos().observe(getViewLifecycleOwner(), pedidos -> {
            pedidoAdapter.setPedidos(pedidos);
        });
    }
}