package sv.edu.ues.pedidosapp.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import sv.edu.ues.pedidosapp.data.local.dao.DetallePedidoDao;
import sv.edu.ues.pedidosapp.data.local.db.AppDatabase;
import sv.edu.ues.pedidosapp.data.local.entity.DetallePedido;

public class DetallePedidoRepository extends BaseRepository {

    private DetallePedidoDao detallePedidoDao;

    public DetallePedidoRepository(Application application) {
        super(application);
        detallePedidoDao = database.detallePedidoDao();
    }

    // Obtener detalles por pedido
    public LiveData<List<DetallePedido>> getDetallesByPedido(int idPedido) {
        return detallePedidoDao.getDetallesByPedido(idPedido);
    }

    // Obtener detalles con información del producto
    public LiveData<List<DetallePedidoDao.DetallePedidoConProducto>> getDetallesConProducto(int idPedido) {
        return detallePedidoDao.getDetallesConProducto(idPedido);
    }

    // Obtener detalle por ID
    public LiveData<DetallePedido> getDetalleById(int idDetalle) {
        return detallePedidoDao.getDetalleById(idDetalle);
    }

    // Insertar detalle
    public CompletableFuture<Long> insertDetallePedido(DetallePedido detallePedido) {
        return CompletableFuture.supplyAsync(() -> {
            return detallePedidoDao.insertDetallePedido(detallePedido);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Insertar múltiples detalles
    public CompletableFuture<Void> insertDetallesPedido(List<DetallePedido> detallesPedido) {
        return CompletableFuture.runAsync(() -> {
            detallePedidoDao.insertDetallesPedido(detallesPedido);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Actualizar detalle
    public CompletableFuture<Integer> updateDetallePedido(DetallePedido detallePedido) {
        return CompletableFuture.supplyAsync(() -> {
            return detallePedidoDao.updateDetallePedido(detallePedido);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Actualizar cantidad de un detalle
    public CompletableFuture<Integer> updateCantidadDetalle(int idDetalle, int cantidad, double subtotal) {
        return CompletableFuture.supplyAsync(() -> {
            return detallePedidoDao.updateCantidadDetalle(idDetalle, cantidad, subtotal);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Eliminar detalle
    public CompletableFuture<Integer> deleteDetallePedido(DetallePedido detallePedido) {
        return CompletableFuture.supplyAsync(() -> {
            return detallePedidoDao.deleteDetallePedido(detallePedido);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Eliminar todos los detalles de un pedido
    public CompletableFuture<Integer> deleteDetallesByPedido(int idPedido) {
        return CompletableFuture.supplyAsync(() -> {
            return detallePedidoDao.deleteDetallesByPedido(idPedido);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Obtener cantidad total de productos en un pedido
    public LiveData<Integer> getTotalCantidadByPedido(int idPedido) {
        return detallePedidoDao.getTotalCantidadByPedido(idPedido);
    }

    // Obtener productos más vendidos
    public LiveData<List<DetallePedidoDao.ProductoVendido>> getProductosMasVendidos(int limite) {
        return detallePedidoDao.getProductosMasVendidos(limite);
    }

    // Verificar si un producto está en algún pedido
    public CompletableFuture<Boolean> isProductoEnPedidos(int idProducto) {
        return CompletableFuture.supplyAsync(() -> {
            return detallePedidoDao.countDetallesByProducto(idProducto) > 0;
        }, AppDatabase.databaseWriteExecutor);
    }

    // Obtener detalles por pedido (síncrono)
    public CompletableFuture<List<DetallePedido>> getDetallesByPedidoSync(int idPedido) {
        return CompletableFuture.supplyAsync(() -> {
            return detallePedidoDao.getDetallesByPedidoSync(idPedido);
        }, AppDatabase.databaseWriteExecutor);
    }
}