package sv.edu.ues.pedidosapp.features.productos.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.data.local.entity.Producto;
import sv.edu.ues.pedidosapp.features.core.ViewModelFactory;
import sv.edu.ues.pedidosapp.features.productos.viewmodel.ProductoViewModel;

public class AgregarProductoDialogFragment extends DialogFragment {

    private EditText nombreEditText, precioEditText, imagenUrlEditText;
    private Spinner categoriaSpinner;
    private ProductoViewModel productoViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_agregar_producto, null);

        // Inicializar ViewModel
        ViewModelFactory factory = new ViewModelFactory(getActivity().getApplication());
        productoViewModel = new ViewModelProvider(this, factory).get(ProductoViewModel.class);

        // Inicializar vistas
        nombreEditText = view.findViewById(R.id.edit_text_nombre);
        precioEditText = view.findViewById(R.id.edit_text_precio);
        imagenUrlEditText = view.findViewById(R.id.edit_text_imagen_url);
        categoriaSpinner = view.findViewById(R.id.spinner_categoria);

        builder.setView(view)
                .setTitle("Agregar Producto")
                .setPositiveButton("Agregar", (dialog, id) -> {
                    // Obtener datos del formulario
                    String nombre = nombreEditText.getText().toString().trim();
                    String precioStr = precioEditText.getText().toString().trim();
                    String imagenUrl = imagenUrlEditText.getText().toString().trim();
                    String categoria = categoriaSpinner.getSelectedItem().toString();

                    if (nombre.isEmpty() || precioStr.isEmpty() || imagenUrl.isEmpty()) {
                        Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double precio = Double.parseDouble(precioStr);

                    // Crear nuevo producto
                    Producto producto = new Producto();
                    producto.setNombre(nombre);
                    producto.setPrecio(precio);
                    producto.setImagenUrl(imagenUrl);
                    producto.setCategoria(categoria);
                    producto.setDisponible(true);

                    // Insertar producto
                    productoViewModel.insertProducto(producto);
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    // Cancelar el diálogo
                    dialog.dismiss();
                });

        return builder.create();
    }
}