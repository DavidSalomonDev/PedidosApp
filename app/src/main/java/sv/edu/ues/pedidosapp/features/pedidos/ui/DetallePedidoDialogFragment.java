package sv.edu.ues.pedidosapp.features.pedidos.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.features.core.ViewModelFactory;
import sv.edu.ues.pedidosapp.features.pedidos.viewmodel.PedidoViewModel;

public class DetallePedidoDialogFragment extends DialogFragment {

    private static final String ARG_ID_PEDIDO = "id_pedido";

    private TextView idTextView, fechaTextView, estadoTextView, totalTextView;
    private PedidoViewModel pedidoViewModel;
    private int idPedido;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public static DetallePedidoDialogFragment newInstance(int idPedido) {
        DetallePedidoDialogFragment fragment = new DetallePedidoDialogFragment();
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
        View view = inflater.inflate(R.layout.dialog_detalle_pedido, null);

        // Inicializar ViewModel
        ViewModelFactory factory = new ViewModelFactory(getActivity().getApplication());
        pedidoViewModel = new ViewModelProvider(this, factory).get(PedidoViewModel.class);

        // Inicializar vistas
        idTextView = view.findViewById(R.id.text_view_id);
        fechaTextView = view.findViewById(R.id.text_view_fecha);
        estadoTextView = view.findViewById(R.id.text_view_estado);
        totalTextView = view.findViewById(R.id.text_view_total);

        // Cargar datos del pedido
        pedidoViewModel.getPedidoById(idPedido).observe(this, pedido -> {
            if (pedido != null) {
                idTextView.setText("Pedido #" + pedido.getIdPedido());
                fechaTextView.setText(dateFormat.format(new Date(pedido.getFechaPedido())));
                estadoTextView.setText("Estado: " + pedido.getEstado());
                totalTextView.setText(String.format("Total: $%.2f", pedido.getTotal()));
            }
        });

        builder.setView(view)
                .setTitle("Detalle del Pedido")
                .setPositiveButton("Cerrar", (dialog, id) -> {
                    // Cerrar el diálogo
                    dialog.dismiss();
                });

        return builder.create();
    }
}