package sv.edu.ues.pedidosapp.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import sv.edu.ues.pedidosapp.data.local.dao.PedidoDao;
import sv.edu.ues.pedidosapp.data.local.dao.DetallePedidoDao;
import sv.edu.ues.pedidosapp.data.local.db.AppDatabase;
import sv.edu.ues.pedidosapp.data.local.entity.Pedido;
import sv.edu.ues.pedidosapp.data.local.entity.DetallePedido;

public class PedidoRepository extends BaseRepository {

    private PedidoDao pedidoDao;
    private DetallePedidoDao detallePedidoDao;
    private LiveData<List<Pedido>> allPedidos;

    public PedidoRepository(Application application) {
        super(application);
        pedidoDao = database.pedidoDao();
        detallePedidoDao = database.detallePedidoDao();
        allPedidos = pedidoDao.getAllPedidos();
    }

    // Obtener todos los pedidos
    public LiveData<List<Pedido>> getAllPedidos() {
        return allPedidos;
    }

    // Obtener pedidos por usuario
    public LiveData<List<Pedido>> getPedidosByUsuario(int idUsuario) {
        return pedidoDao.getPedidosByUsuario(idUsuario);
    }

    // Obtener pedido por ID
    public LiveData<Pedido> getPedidoById(int idPedido) {
        return pedidoDao.getPedidoById(idPedido);
    }

    // Obtener pedidos por estado
    public LiveData<List<Pedido>> getPedidosByEstado(String estado) {
        return pedidoDao.getPedidosByEstado(estado);
    }

    // Obtener pedidos por usuario y estado
    public LiveData<List<Pedido>> getPedidosByUsuarioAndEstado(int idUsuario, String estado) {
        return pedidoDao.getPedidosByUsuarioAndEstado(idUsuario, estado);
    }

    // Insertar pedido completo (pedido + detalles)
    public CompletableFuture<PedidoResult> insertPedidoCompleto(Pedido pedido, List<DetallePedido> detalles) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Insertar pedido
                long pedidoId = pedidoDao.insertPedido(pedido);

                if (pedidoId > 0) {
                    // Actualizar ID del pedido en los detalles
                    for (DetallePedido detalle : detalles) {
                        detalle.setIdPedido((int) pedidoId);
                    }

                    // Insertar detalles
                    detallePedidoDao.insertDetallesPedido(detalles);

                    return new PedidoResult(true, "Pedido creado exitosamente", pedidoId);
                } else {
                    return new PedidoResult(false, "Error al crear pedido", -1);
                }
            } catch (Exception e) {
                return new PedidoResult(false, "Error: " + e.getMessage(), -1);
            }
        }, AppDatabase.databaseWriteExecutor);
    }

    // Actualizar pedido
    public CompletableFuture<Integer> updatePedido(Pedido pedido) {
        return CompletableFuture.supplyAsync(() -> {
            return pedidoDao.updatePedido(pedido);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Actualizar estado del pedido
    public CompletableFuture<Integer> updateEstadoPedido(int idPedido, String estado) {
        return CompletableFuture.supplyAsync(() -> {
            return pedidoDao.updateEstadoPedido(idPedido, estado);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Eliminar pedido completo (pedido + detalles)
    public CompletableFuture<Boolean> deletePedidoCompleto(int idPedido) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Eliminar detalles primero (por foreign key)
                detallePedidoDao.deleteDetallesByPedido(idPedido);

                // Eliminar pedido
                int result = pedidoDao.deletePedidoById(idPedido);

                return result > 0;
            } catch (Exception e) {
                return false;
            }
        }, AppDatabase.databaseWriteExecutor);
    }

    // Obtener pedidos por rango de fechas
    public LiveData<List<Pedido>> getPedidosByFechaRange(long fechaInicio, long fechaFin) {
        return pedidoDao.getPedidosByFechaRange(fechaInicio, fechaFin);
    }

    // Obtener total de ventas por usuario
    public LiveData<Double> getTotalVentasByUsuario(int idUsuario) {
        return pedidoDao.getTotalVentasByUsuario(idUsuario);
    }

    // Contar pedidos por estado
    public LiveData<Integer> countPedidosByEstado(String estado) {
        return pedidoDao.countPedidosByEstado(estado);
    }

    // Obtener últimos pedidos
    public LiveData<List<Pedido>> getUltimosPedidos(int limite) {
        return pedidoDao.getUltimosPedidos(limite);
    }

    // Clase para resultado de operaciones de pedido
    public static class PedidoResult {
        private boolean success;
        private String message;
        private long pedidoId;

        public PedidoResult(boolean success, String message, long pedidoId) {
            this.success = success;
            this.message = message;
            this.pedidoId = pedidoId;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public long getPedidoId() { return pedidoId; }
    }
}