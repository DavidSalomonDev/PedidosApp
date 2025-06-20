//package sv.edu.ues.pedidosapp.features.productos.ui;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//import androidx.lifecycle.ViewModelProvider;
//
//import sv.edu.ues.pedidosapp.R;
//import sv.edu.ues.pedidosapp.features.core.ViewModelFactory;
//import sv.edu.ues.pedidosapp.features.productos.viewmodel.ProductoViewModel;
//
//public class EditarProductoDialogFragment extends DialogFragment {
//
//    private static final String ARG_ID_PRODUCTO = "id_producto";
//
//    private EditText nombreEditText, precioEditText, imagenUrlEditText;
//    private Spinner categoriaSpinner;
//    private ProductoViewModel productoViewModel;
//    private int idProducto;
//
//    public static EditarProductoDialogFragment newInstance(int idProducto) {
//        EditarProductoDialogFragment fragment = new EditarProductoDialogFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_ID_PRODUCTO, idProducto);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            idProducto = getArguments().getInt(ARG_ID_PRODUCTO);
//        }
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.dialog_editar_producto, null);
//
//        // Inicializar ViewModel
//        ViewModelFactory factory = new ViewModelFactory(getActivity().getApplication());
//        productoViewModel = new ViewModelProvider(this, factory).get(ProductoViewModel.class);
//
//        // Inicializar vistas
//        nombreEditText = view.findViewById(R.id.edit_text_nombre);
//        precioEditText = view.findViewById(R.id.edit_text_precio);
//        imagenUrlEditText = view.findViewById(R.id.edit_text_imagen_url);
//        categoriaSpinner = view.findViewById(R.id.spinner_categoria);
//
//        // Cargar datos del producto
//        productoViewModel.getProductoById(idProducto).observe(this, producto -> {
//            if (producto != null) {
//                nombreEditText.setText(producto.getNombre());
//                precioEditText.setText(String.valueOf(producto.getPrecio()));
//                imagenUrlEditText.setText(producto.getImagenUrl());
//                // TODO: Seleccionar la categoría en el Spinner
//            }
//        });
//
//        builder.setView(view)
//                .setTitle("Editar Producto")
//                .setPositiveButton("Guardar", (dialog, id) -> {
//                    // Obtener datos del formulario
//                    String nombre = nombreEditText.getText().toString().trim();
//                    String precioStr = precioEditText.getText().toString().trim();
//                    String imagenUrl = imagenUrlEditText.getText().toString().trim();
//                    String categoria = categoriaSpinner.getSelectedItem().toString();
//
//                    if (nombre.isEmpty() || precioStr.isEmpty() || imagenUrl.isEmpty()) {
//                        Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    double precio = Double.parseDouble(precioStr);
//
//                    // Actualizar producto
//                    productoViewModel.getProductoById(idProducto).observe(this, producto -> {
//                        if (producto != null) {
//                            producto.setNombre(nombre);
//                            producto.setPrecio(precio);
//                            producto.setImagenUrl(imagenUrl);
//                            producto.setCategoria(categoria);
//                            productoViewModel.updateProducto(producto);
//                        }
//                    });
//                })
//                .setNegativeButton("Cancelar", (dialog, id) -> {
//                    // Cancelar el diálogo
//                    dialog.dismiss();
//                });
//
//        return builder.create();
//    }
//}