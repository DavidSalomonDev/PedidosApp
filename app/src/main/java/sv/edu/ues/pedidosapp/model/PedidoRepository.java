// PedidoRepository.java
package sv.edu.ues.pedidosapp.model;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

import sv.edu.ues.pedidosapp.database.AppDatabase;
import sv.edu.ues.pedidosapp.dao.PedidoDao;
import sv.edu.ues.pedidosapp.dao.DetallePedidoDao;
import sv.edu.ues.pedidosapp.dao.ProductoDao;
import sv.edu.ues.pedidosapp.entity.Pedido;
import sv.edu.ues.pedidosapp.entity.DetallePedido;
import sv.edu.ues.pedidosapp.entity.DetallePedidoConProducto;

public class PedidoRepository {
    private PedidoDao pedidoDao;
    private DetallePedidoDao detallePedidoDao;
    private ProductoDao productoDao;

    private LiveData<List<Pedido>> allPedidos;

    public PedidoRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        pedidoDao = database.pedidoDao();
        detallePedidoDao = database.detallePedidoDao();
        productoDao = database.productoDao();
        allPedidos = pedidoDao.getAllPedidos();
    }

    // Métodos para Pedidos
    public LiveData<List<Pedido>> getAllPedidos() {
        return allPedidos;
    }

    public LiveData<List<Pedido>> getPedidosByUsuario(int usuarioId) {
        return pedidoDao.getPedidosByUsuario(usuarioId);
    }

    public LiveData<Pedido> getPedidoById(int id) {
        return pedidoDao.getPedidoById(id);
    }

    public LiveData<List<Pedido>> getPedidosByEstado(String estado) {
        return pedidoDao.getPedidosByEstado(estado);
    }

    public void insertPedido(Pedido pedido, List<DetallePedido> detalles) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            long pedidoId = pedidoDao.insertPedido(pedido);
            for (DetallePedido detalle : detalles) {
                detalle.setPedidoId((int) pedidoId);
            }
            detallePedidoDao.insertDetalles(detalles);
        });
    }

    public void updatePedido(Pedido pedido) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            pedidoDao.updatePedido(pedido);
        });
    }

    public void deletePedido(Pedido pedido) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            detallePedidoDao.deleteDetallesByPedido(pedido.getId());
            pedidoDao.deletePedido(pedido);
        });
    }

    // Métodos para Detalles
    public LiveData<List<DetallePedidoConProducto>> getDetallesConProducto(int pedidoId) {
        return detallePedidoDao.getDetallesConProducto(pedidoId);
    }
}