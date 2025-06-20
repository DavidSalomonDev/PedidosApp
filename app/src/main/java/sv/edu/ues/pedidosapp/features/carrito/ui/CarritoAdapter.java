// sv/edu/ues/pedidosapp/features/carrito/ui/CarritoAdapter.java
package sv.edu.ues.pedidosapp.features.carrito.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.data.model.ProductoSeleccionado;
import sv.edu.ues.pedidosapp.features.carrito.viewmodel.CarritoViewModel;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private Context context;
    private List<ProductoSeleccionado> productos;
    private CarritoViewModel carritoViewModel;

    public CarritoAdapter(Context context, CarritoViewModel carritoViewModel) {
        this.context = context;
        this.carritoViewModel = carritoViewModel;
    }

    public void setProductos(List<ProductoSeleccionado> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        ProductoSeleccionado item = productos.get(position);

        holder.nombreTextView.setText(item.getProducto().getNombre());
        holder.precioTextView.setText(String.format("$%.2f", item.getProducto().getPrecio()));
        holder.cantidadTextView.setText(String.valueOf(item.getCantidad()));
        holder.subtotalTextView.setText(String.format("$%.2f", item.getSubtotal()));
        holder.btnEliminar.setOnClickListener(v -> {
            carritoViewModel.eliminarProducto(item.getProducto().getIdProducto());
        });

        // Cargar imagen con Picasso
        Picasso.get()
                .load(item.getProducto().getImagenUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imagenImageView);
    }

    @Override
    public int getItemCount() {
        return productos == null ? 0 : productos.size();
    }

    static class CarritoViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenImageView;
        TextView nombreTextView;
        TextView precioTextView;
        TextView cantidadTextView;
        TextView subtotalTextView;
        ImageButton btnEliminar;

        CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenImageView = itemView.findViewById(R.id.item_carrito_imagen);
            nombreTextView = itemView.findViewById(R.id.item_carrito_nombre);
            precioTextView = itemView.findViewById(R.id.item_carrito_precio);
            cantidadTextView = itemView.findViewById(R.id.item_carrito_cantidad);
            subtotalTextView = itemView.findViewById(R.id.item_carrito_subtotal);
            btnEliminar = itemView.findViewById(R.id.btn_eliminar);
        }
    }
}