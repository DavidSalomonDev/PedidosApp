package sv.edu.ues.pedidosapp.features.productos.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.data.local.entity.Producto;

public class ProductoAdapter extends PagingDataAdapter<Producto, ProductoAdapter.ProductoViewHolder> {

    private static final DiffUtil.ItemCallback<Producto> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Producto>() {
                @Override
                public boolean areItemsTheSame(@NonNull Producto oldItem, @NonNull Producto newItem) {
                    return oldItem.getIdProducto() == newItem.getIdProducto();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Producto oldItem, @NonNull Producto newItem) {
                    return oldItem.equals(newItem);
                }
            };
    private final Context context;
    private final OnProductoClickListener listener;

    public ProductoAdapter(Context context, OnProductoClickListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto_seleccion, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = getItem(position);

        if (producto != null) {
            holder.nombreTextView.setText(producto.getNombre());
            holder.precioTextView.setText(String.format("$%.2f", producto.getPrecio()));
            holder.categoriaTextView.setText(producto.getCategoria());

            // Cargar imagen con Picasso
            Picasso.get()
                    .load(producto.getImagenUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.imagenImageView);

//            // Listener para el botón de editar
//            holder.editarImageView.setOnClickListener(v -> {
//                if (listener != null) {
//                    listener.onEditarProducto(producto.getIdProducto());
//                }
//            });

//            // Listener para el botón de eliminar
//            holder.eliminarImageView.setOnClickListener(v -> {
//                if (listener != null) {
//                    listener.onEliminarProducto(producto.getIdProducto());
//                }
//            });

            // Listener para el botón de agregar al carrito
            holder.agregarCarritoButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAgregarAlCarrito(producto);
                }
            });
        } else {
            // Null item
            holder.nombreTextView.setText("Cargando...");
            holder.precioTextView.setText("");
            holder.categoriaTextView.setText("");
            holder.imagenImageView.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    public interface OnProductoClickListener {
//        void onEditarProducto(int idProducto);
//
//        void onEliminarProducto(int idProducto);

        void onAgregarAlCarrito(Producto producto);
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenImageView;
        TextView nombreTextView;
        TextView precioTextView;
        TextView categoriaTextView;
        //        ImageView editarImageView;
//        ImageView eliminarImageView;
        Button agregarCarritoButton;

        ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenImageView = itemView.findViewById(R.id.item_producto_imagen);
            nombreTextView = itemView.findViewById(R.id.item_producto_nombre);
            precioTextView = itemView.findViewById(R.id.item_producto_precio);
            categoriaTextView = itemView.findViewById(R.id.item_producto_categoria);
//            editarImageView = itemView.findViewById(R.id.item_producto_editar);
//            eliminarImageView = itemView.findViewById(R.id.item_producto_eliminar);
            agregarCarritoButton = itemView.findViewById(R.id.btn_agregar_carrito);
        }
    }
}