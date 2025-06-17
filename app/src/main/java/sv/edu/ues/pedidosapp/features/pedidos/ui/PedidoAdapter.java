package sv.edu.ues.pedidosapp.features.pedidos.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.data.local.entity.Pedido;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private Context context;
    private List<Pedido> pedidos;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public PedidoAdapter(Context context) {
        this.context = context;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        holder.idTextView.setText(String.valueOf(pedido.getIdPedido()));
        holder.fechaTextView.setText(dateFormat.format(new Date(pedido.getFechaPedido())));
        holder.estadoTextView.setText(pedido.getEstado());
        holder.totalTextView.setText(String.format("$%.2f", pedido.getTotal()));
    }

    @Override
    public int getItemCount() {
        return pedidos == null ? 0 : pedidos.size();
    }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView;
        TextView fechaTextView;
        TextView estadoTextView;
        TextView totalTextView;

        PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.item_pedido_id);
            fechaTextView = itemView.findViewById(R.id.item_pedido_fecha);
            estadoTextView = itemView.findViewById(R.id.item_pedido_estado);
            totalTextView = itemView.findViewById(R.id.item_pedido_total);
        }
    }
}