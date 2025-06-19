// sv/edu/ues/pedidosapp/data/repository/DetallePedidoRepository.java
package sv.edu.ues.pedidosapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import sv.edu.ues.pedidosapp.data.local.AppDatabase;
import sv.edu.ues.pedidosapp.data.local.dao.DetallePedidoDao;
import sv.edu.ues.pedidosapp.data.local.entity.DetallePedido;

public class DetallePedidoRepository {

    private DetallePedidoDao detallePedidoDao;
    private Executor executor = Executors.newSingleThreadExecutor();

    public DetallePedidoRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        detallePedidoDao = database.detallePedidoDao();
    }

    // Insertar detalle de pedido
    public CompletableFuture<Long> insertDetallePedido(DetallePedido detallePedido) {
        return CompletableFuture.supplyAsync(() -> {
            return detallePedidoDao.insertDetallePedido(detallePedido);
        }, executor);
    }

    // Actualizar detalle de pedido
    public CompletableFuture<Integer> updateDetallePedido(DetallePedido detallePedido) {
        return CompletableFuture.supplyAsync(() -> {
            return detallePedidoDao.updateDetallePedido(detallePedido);
        }, executor);
    }

    // Eliminar detalle de pedido
    public CompletableFuture<Integer> deleteDetallePedido(DetallePedido detallePedido) {
        return CompletableFuture.supplyAsync(() -> {
            return detallePedidoDao.deleteDetallePedido(detallePedido);
        }, executor);
    }

    // Obtener detalles de un pedido específico
    public LiveData<List<DetallePedido>> getDetallesByPedido(int idPedido) {
        return detallePedidoDao.getDetallesByPedido(idPedido);
    }

    // Obtener detalle por ID
    public LiveData<DetallePedido> getDetalleById(int idDetalle) {
        return detallePedidoDao.getDetalleById(idDetalle);
    }

    // Eliminar todos los detalles de un pedido
    public CompletableFuture<Integer> deleteDetallesByPedido(int idPedido) {
        return CompletableFuture.supplyAsync(() -> {
            return detallePedidoDao.deleteDetallesByPedido(idPedido);
        }, executor);
    }

    // Obtener la suma total de un pedido
    public LiveData<Double> getTotalPedido(int idPedido) {
        return detallePedidoDao.getTotalPedido(idPedido);
    }
}