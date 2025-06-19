// sv/edu/ues/pedidosapp/features/productos/ui/SeleccionarCantidadDialogFragment.java
package sv.edu.ues.pedidosapp.features.productos.ui;

import android.app.AlertDialog;
import android.app.Dialog;
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

import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.data.local.entity.Producto;
import sv.edu.ues.pedidosapp.features.carrito.viewmodel.CarritoViewModel;

public class SeleccionarCantidadDialogFragment extends DialogFragment {

    private static final String ARG_PRODUCTO = "producto";

    private Producto producto;
    private EditText cantidadEditText;
    private TextView nombreTextView, precioTextView;
    private CarritoViewModel carritoViewModel;

    public static SeleccionarCantidadDialogFragment newInstance(Producto producto) {
        SeleccionarCantidadDialogFragment fragment = new SeleccionarCantidadDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCTO, producto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            producto = (Producto) getArguments().getSerializable(ARG_PRODUCTO);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_seleccionar_cantidad, null);

        // Inicializar ViewModel
        carritoViewModel = new ViewModelProvider(requireActivity()).get(CarritoViewModel.class);

        // Inicializar vistas
        nombreTextView = view.findViewById(R.id.text_view_nombre_producto);
        precioTextView = view.findViewById(R.id.text_view_precio_producto);
        cantidadEditText = view.findViewById(R.id.edit_text_cantidad);

        // Mostrar información del producto
        if (producto != null) {
            nombreTextView.setText(producto.getNombre());
            precioTextView.setText(String.format("$%.2f", producto.getPrecio()));
        }

        builder.setView(view)
                .setTitle("Agregar al Carrito")
                .setPositiveButton("Agregar", (dialog, id) -> {
                    String cantidadStr = cantidadEditText.getText().toString().trim();

                    if (cantidadStr.isEmpty()) {
                        Toast.makeText(getContext(), "Por favor, ingrese la cantidad", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int cantidad = Integer.parseInt(cantidadStr);

                    if (cantidad <= 0) {
                        Toast.makeText(getContext(), "La cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Agregar producto al carrito
                    carritoViewModel.agregarProducto(producto, cantidad);
                    Toast.makeText(getContext(), "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    dialog.dismiss();
                });

        return builder.create();
    }
}