package sv.edu.ues.pedidosapp.features.pedidos.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import sv.edu.ues.pedidosapp.features.core.ViewModelFactory;
import sv.edu.ues.pedidosapp.features.pedidos.viewmodel.PedidoViewModel;

public class EditarPedidoDialogFragment extends DialogFragment {

    private static final String ARG_ID_PEDIDO = "id_pedido";

    private EditText totalEditText;
    private PedidoViewModel pedidoViewModel;
    private int idPedido;

    public static EditarPedidoDialogFragment newInstance(int idPedido) {
        EditarPedidoDialogFragment fragment = new EditarPedidoDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID_PEDIDO, idPedido);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idPedido = getArguments().getInt(ARG_ID_PEDIDO);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //View view = inflater.inflate(R.layout.dialog_editar_pedido, null);

        // Inicializar ViewModel
        ViewModelFactory factory = new ViewModelFactory(getActivity().getApplication());
        pedidoViewModel = new ViewModelProvider(this, factory).get(PedidoViewModel.class);

        // Inicializar vistas
        //totalEditText = view.findViewById(R.id.edit_text_total);

        // Cargar datos del pedido
        pedidoViewModel.getPedidoById(idPedido).observe(this, pedido -> {
            if (pedido != null) {
                totalEditText.setText(String.valueOf(pedido.getTotal()));
            }
        });

//        builder.setView(view)
//                .setTitle("Editar Pedido")
//                .setPositiveButton("Guardar", (dialog, id) -> {
//                    // Obtener datos del formulario
//                    String totalStr = totalEditText.getText().toString().trim();
//
//                    if (totalStr.isEmpty()) {
//                        Toast.makeText(getContext(), "Por favor, ingrese el total del pedido", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    double total = Double.parseDouble(totalStr);
//
//                    // Actualizar pedido
//                    pedidoViewModel.getPedidoById(idPedido).observe(this, pedido -> {
//                        if (pedido != null) {
//                            pedido.setTotal(total);
//                            pedidoViewModel.updatePedido(pedido);
//                        }
//                    });
//                })
//                .setNegativeButton("Cancelar", (dialog, id) -> {
//                    // Cancelar el diálogo
//                    dialog.dismiss();
//                });
//
        return builder.create();
    }
}