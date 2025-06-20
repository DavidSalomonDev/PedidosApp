package sv.edu.ues.pedidosapp.features.detallepedido.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.data.local.entity.DetallePedido;

public class DetallePedidoAdapter extends RecyclerView.Adapter<DetallePedidoAdapter.ViewHolder> {

    private List<DetallePedido> detalles = new ArrayList<>();

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalle_pedido, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DetallePedido detalle = detalles.get(position);
        holder.nombreProducto.setText("Producto ID: " + detalle.getIdProducto());
        holder.cantidad.setText("Cantidad: " + detalle.getCantidad());
        holder.precio.setText("Precio: $" + detalle.getPrecioUnitario());
        holder.subtotal.setText("Subtotal: $" + detalle.getSubtotal());
    }

    @Override
    public int getItemCount() {
        return detalles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreProducto, cantidad, precio, subtotal;

        ViewHolder(View itemView) {
            super(itemView);
            nombreProducto = itemView.findViewById(R.id.textNombreProducto);
            cantidad = itemView.findViewById(R.id.textCantidad);
            precio = itemView.findViewById(R.id.textPrecio);
            subtotal = itemView.findViewById(R.id.textSubtotal);
        }
    }
}