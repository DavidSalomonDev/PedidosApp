package sv.edu.ues.pedidosapp.features.productos.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.data.local.entity.Producto;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private Context context;
    private List<Producto> productos;

    public ProductoAdapter(Context context) {
        this.context = context;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.nombreTextView.setText(producto.getNombre());
        holder.precioTextView.setText(String.format("$%.2f", producto.getPrecio()));

        // Cargar imagen con Picasso
        Picasso.get()
                .load(producto.getImagenUrl())
                .placeholder(R.drawable.ic_launcher_background) // Imagen por defecto
                .error(R.drawable.ic_launcher_background) // Imagen en caso de error
                .into(holder.imagenImageView);
    }

    @Override
    public int getItemCount() {
        return productos == null ? 0 : productos.size();
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenImageView;
        TextView nombreTextView;
        TextView precioTextView;

        ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenImageView = itemView.findViewById(R.id.item_producto_imagen);
            nombreTextView = itemView.findViewById(R.id.item_producto_nombre);
            precioTextView = itemView.findViewById(R.id.item_producto_precio);
        }
    }
}