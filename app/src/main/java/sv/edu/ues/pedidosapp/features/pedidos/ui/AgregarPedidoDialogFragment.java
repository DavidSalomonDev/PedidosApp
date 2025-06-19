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

        // Validaciones
        if (cliente.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, ingrese el nombre del cliente y la dirección", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar que hay productos en el carrito
        List<ProductoSeleccionado> productosSeleccionados = carritoViewModel.getProductosSeleccionados().getValue();
        if (productosSeleccionados == null || productosSeleccionados.isEmpty()) {
            Toast.makeText(getContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener ID del usuario logueado
        long userId = sharedPreferences.getLong(Constants.PREF_USER_ID, -1);
        if (userId == -1) {
            Toast.makeText(getContext(), "Debe iniciar sesión para crear un pedido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calcular total
        Double total = carritoViewModel.getTotalCarrito().getValue();
        if (total == null) total = 0.0;

        // Crear nuevo pedido
        Pedido pedido = new Pedido();
        pedido.setIdUsuario((int) userId);
        pedido.setDireccionEntrega(direccion);
        pedido.setNotas(cliente);
        pedido.setFechaPedido(new Date().getTime());
        pedido.setTotal(total);
        pedido.setEstado(Constants.ESTADO_PENDIENTE);

        // Insertar pedido
        pedidoViewModel.insertPedido(pedido);

        // Observar el resultado de la inserción del pedido
        pedidoViewModel.getOperationResult().observe(this, result -> {
            if (result != null && result.contains("exitosamente")) {
                // Obtener el último pedido insertado para obtener su ID
                pedidoViewModel.getAllPedidos().observe(this, pedidos -> {
                    if (pedidos != null && !pedidos.isEmpty()) {
                        // Obtener el ID del pedido recién creado
                        Pedido ultimoPedido = pedidos.get(pedidos.size() - 1);
                        int idPedido = ultimoPedido.getIdPedido();

                        // Crear detalles del pedido
                        for (ProductoSeleccionado item : productosSeleccionados) {
                            DetallePedido detalle = new DetallePedido();
                            detalle.setIdPedido(idPedido);
                            detalle.setIdProducto(item.getProducto().getIdProducto());
                            detalle.setCantidad(item.getCantidad());
                            detalle.setPrecioUnitario(item.getProducto().getPrecio());
                            detalle.setSubtotal(item.getSubtotal());

                            detallePedidoViewModel.insertDetallePedido(detalle);
                        }

                        // Limpiar el carrito
                        carritoViewModel.limpiarCarrito();

                        Toast.makeText(getContext(), "Pedido creado exitosamente", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
            } else if (result != null) {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }
        });
    }
}