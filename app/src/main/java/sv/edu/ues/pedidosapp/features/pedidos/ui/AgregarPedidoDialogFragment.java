package sv.edu.ues.pedidosapp.features.pedidos.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;
import java.util.List;

import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.data.local.entity.DetallePedido;
import sv.edu.ues.pedidosapp.data.local.entity.Pedido;
import sv.edu.ues.pedidosapp.data.model.ProductoSeleccionado;
import sv.edu.ues.pedidosapp.features.carrito.viewmodel.CarritoViewModel;
import sv.edu.ues.pedidosapp.features.core.ViewModelFactory;
import sv.edu.ues.pedidosapp.features.detallepedido.viewmodel.DetallePedidoViewModel;
import sv.edu.ues.pedidosapp.features.pedidos.viewmodel.PedidoViewModel;
import sv.edu.ues.pedidosapp.utils.Constants;
import sv.edu.ues.pedidosapp.utils.SessionManager;

public class AgregarPedidoDialogFragment extends DialogFragment {

    private EditText clienteEditText, direccionEditText;
    private TextView totalTextView;
    private PedidoViewModel pedidoViewModel;
    private DetallePedidoViewModel detallePedidoViewModel;
    private CarritoViewModel carritoViewModel;
    private SharedPreferences sharedPreferences;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_agregar_pedido, null);

        // Inicializar SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);

        // Inicializar ViewModels
        ViewModelFactory factory = new ViewModelFactory(getActivity().getApplication());
        pedidoViewModel = new ViewModelProvider(this, factory).get(PedidoViewModel.class);
        detallePedidoViewModel = new ViewModelProvider(this, factory).get(DetallePedidoViewModel.class);
        carritoViewModel = new ViewModelProvider(requireActivity()).get(CarritoViewModel.class);

        // Inicializar vistas
        clienteEditText = view.findViewById(R.id.edit_text_cliente);
        direccionEditText = view.findViewById(R.id.edit_text_direccion);
        totalTextView = view.findViewById(R.id.text_view_total);

        SessionManager sessionManager = new SessionManager(requireContext());
        if (sessionManager.isLoggedIn()) {
            String nombre = sessionManager.getUserName();
            String direccion = sessionManager.getUserAddress();

            if (nombre != null && !nombre.isEmpty()) {
                clienteEditText.setText(nombre);
            }
            if (direccion != null && !direccion.isEmpty()) {
                direccionEditText.setText(direccion);
            }
        }

        // Mostrar el total del carrito
        carritoViewModel.getTotalCarrito().observe(this, total -> {
            if (total != null) {
                totalTextView.setText(String.format("Total: $%.2f", total));
            }
        });

        builder.setView(view)
                .setTitle("Crear Pedido")
                .setPositiveButton("Crear", (dialog, id) -> {
                    crearPedido();
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    dialog.dismiss();
                });

        return builder.create();
    }

    private void crearPedido() {
        String cliente = clienteEditText.getText().toString().trim();
        String direccion = direccionEditText.getText().toString().trim();

        if (cliente.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, ingrese el nombre del cliente y la dirección", Toast.LENGTH_SHORT).show();
            return;
        }

        List<ProductoSeleccionado> productosSeleccionados = carritoViewModel.getProductosSeleccionados().getValue();
        if (productosSeleccionados == null || productosSeleccionados.isEmpty()) {
            Toast.makeText(getContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        long userId = sharedPreferences.getLong(Constants.PREF_USER_ID, -1);
        if (userId == -1) {
            Toast.makeText(getContext(), "Debe iniciar sesión para crear un pedido", Toast.LENGTH_SHORT).show();
            return;
        }

        Double total = carritoViewModel.getTotalCarrito().getValue();
        if (total == null) total = 0.0;

        Pedido pedido = new Pedido();
        pedido.setIdUsuario((int) userId);
        pedido.setDireccionEntrega(direccion);
        pedido.setNotas(cliente);
        pedido.setFechaPedido(new Date().getTime());
        pedido.setTotal(total);
        pedido.setEstado(Constants.ESTADO_PENDIENTE);

        // Aquí el cambio: usar el future para obtener el ID insertado
        pedidoViewModel.insertPedidoAndGetId(pedido).thenAccept(idPedido -> {
            if (idPedido > 0) {
                for (ProductoSeleccionado item : productosSeleccionados) {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setIdPedido(idPedido);
                    detalle.setIdProducto(item.getProducto().getIdProducto());
                    detalle.setCantidad(item.getCantidad());
                    detalle.setPrecioUnitario(item.getProducto().getPrecio());
                    detalle.setSubtotal(item.getSubtotal());

                    detallePedidoViewModel.insertDetallePedido(detalle);
                }
                // Limpiar el carrito en el hilo principal
                requireActivity().runOnUiThread(() -> {
                    carritoViewModel.limpiarCarrito();
                    Toast.makeText(getContext(), "Pedido creado exitosamente", Toast.LENGTH_SHORT).show();
                    dismiss();
                });
            } else {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Error al crear el pedido", Toast.LENGTH_SHORT).show();
                });
            }
        }).exceptionally(throwable -> {
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            });
            return null;
        });
    }
}