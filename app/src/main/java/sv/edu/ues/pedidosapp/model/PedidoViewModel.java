package sv.edu.ues.pedidosapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import sv.edu.ues.pedidosapp.entity.DetallePedido;
import sv.edu.ues.pedidosapp.entity.DetallePedidoConProducto;
import sv.edu.ues.pedidosapp.entity.Pedido;

public class PedidoViewModel extends AndroidViewModel {
    private PedidoRepository repository;
    private LiveData<List<Pedido>> allPedidos;

    public PedidoViewModel(@NonNull Application application) {
        super(application);
        repository = new PedidoRepository(application);
        allPedidos = repository.getAllPedidos();
    }

    public LiveData<List<Pedido>> getAllPedidos() {
        return allPedidos;
    }

    public LiveData<List<Pedido>> getPedidosByUsuario(int usuarioId) {
        return repository.getPedidosByUsuario(usuarioId);
    }

    public LiveData<Pedido> getPedidoById(int id) {
        return repository.getPedidoById(id);
    }

    public void insertPedido(Pedido pedido, List<DetallePedido> detalles) {
        repository.insertPedido(pedido, detalles);
    }

    public void updatePedido(Pedido pedido) {
        repository.updatePedido(pedido);
    }

    public void deletePedido(Pedido pedido) {
        repository.deletePedido(pedido);
    }

    public LiveData<List<DetallePedidoConProducto>> getDetallesConProducto(int pedidoId) {
        return repository.getDetallesConProducto(pedidoId);
    }
}