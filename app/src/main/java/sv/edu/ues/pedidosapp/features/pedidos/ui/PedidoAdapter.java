package sv.edu.ues.pedidosapp.features.pedidos.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.data.local.entity.Pedido;
import sv.edu.ues.pedidosapp.utils.Constants;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private final Context context;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private final OnPedidoClickListener listener;
    private List<Pedido> pedidos;

    public PedidoAdapter(Context context, OnPedidoClickListener listener) {
        this.context = context;
        this.listener = listener;
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
        holder.idTextView.setText("Pedido #" + pedido.getIdPedido());
        holder.fechaTextView.setText(dateFormat.format(new Date(pedido.getFechaPedido())));
        holder.estadoTextView.setText("Estado: " + pedido.getEstado());
        holder.totalTextView.setText(String.format("Total: $%.2f", pedido.getTotal()));

        // Click en el item completo
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPedidoClick(pedido.getIdPedido());
            }
        });

        // Botón editar
        holder.btnEditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditarPedido(pedido.getIdPedido());
            }
        });

        // Botón eliminar
        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEliminarPedido(pedido.getIdPedido());
            }
        });

        // Botón cambiar estado (solo si no está entregado o cancelado)
        if (!pedido.getEstado().equals(Constants.ESTADO_ENTREGADO) &&
                !pedido.getEstado().equals(Constants.ESTADO_CANCELADO)) {
            holder.btnCambiarEstado.setVisibility(View.VISIBLE);
            holder.btnCambiarEstado.setOnClickListener(v -> {
                String nuevoEstado = getNextEstado(pedido.getEstado());
                if (listener != null) {
                    listener.onCambiarEstado(pedido.getIdPedido(), nuevoEstado);
                }
            });
        } else {
            holder.btnCambiarEstado.setVisibility(View.GONE);
        }
    }

    private String getNextEstado(String estadoActual) {
        switch (estadoActual) {
            case Constants.ESTADO_PENDIENTE:
                return Constants.ESTADO_CONFIRMADO;
            case Constants.ESTADO_CONFIRMADO:
                return Constants.ESTADO_EN_PREPARACION;
            case Constants.ESTADO_EN_PREPARACION:
                return Constants.ESTADO_ENTREGADO;
            default:
                return estadoActual;
        }
    }

    @Override
    public int getItemCount() {
        return pedidos == null ? 0 : pedidos.size();
    }

    public interface OnPedidoClickListener {
        void onPedidoClick(long idPedido);
        void onEditarPedido(long idPedido);
        void onEliminarPedido(long idPedido);
        void onCambiarEstado(long idPedido, String nuevoEstado);
    }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView;
        TextView fechaTextView;
        TextView estadoTextView;
        TextView totalTextView;
        Button btnEditar;
        Button btnEliminar;
        Button btnCambiarEstado;

        PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.item_pedido_id);
            fechaTextView = itemView.findViewById(R.id.item_pedido_fecha);
            estadoTextView = itemView.findViewById(R.id.item_pedido_estado);
            totalTextView = itemView.findViewById(R.id.item_pedido_total);
            btnEditar = itemView.findViewById(R.id.btn_editar_pedido);
            btnEliminar = itemView.findViewById(R.id.btn_eliminar_pedido);
            btnCambiarEstado = itemView.findViewById(R.id.btn_cambiar_estado);
        }
    }
}