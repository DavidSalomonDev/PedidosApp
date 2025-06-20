// sv/edu/ues/pedidosapp/data/repository/PedidoRepository.java
package sv.edu.ues.pedidosapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import sv.edu.ues.pedidosapp.data.local.AppDatabase;
import sv.edu.ues.pedidosapp.data.local.dao.PedidoDao;
import sv.edu.ues.pedidosapp.data.local.entity.Pedido;

public class PedidoRepository {

    private PedidoDao pedidoDao;
    private Executor executor = Executors.newSingleThreadExecutor();

    public PedidoRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        pedidoDao = database.pedidoDao();
    }

    // Insertar pedido
    public CompletableFuture<Long> insertPedido(Pedido pedido) {
        return CompletableFuture.supplyAsync(() -> {
            return pedidoDao.insertPedido(pedido);
        }, executor);
    }

    // Actualizar pedido
    public CompletableFuture<Integer> updatePedido(Pedido pedido) {
        return CompletableFuture.supplyAsync(() -> {
            return pedidoDao.updatePedido(pedido);
        }, executor);
    }

    // Eliminar pedido
    public CompletableFuture<Integer> deletePedido(Pedido pedido) {
        return CompletableFuture.supplyAsync(() -> {
            return pedidoDao.deletePedido(pedido);
        }, executor);
    }

    // Obtener todos los pedidos
    public LiveData<List<Pedido>> getAllPedidos() {
        return pedidoDao.getAllPedidos();
    }

    // Obtener pedido por ID
    public LiveData<Pedido> getPedidoById(long idPedido) {
        return pedidoDao.getPedidoById(idPedido);
    }

    // Obtener pedidos por estado
    public LiveData<List<Pedido>> getPedidosByEstado(String estado) {
        return pedidoDao.getPedidosByEstado(estado);
    }

    // Actualizar estado del pedido
    public CompletableFuture<Integer> updateEstadoPedido(long idPedido, String estado) {
        return CompletableFuture.supplyAsync(() -> {
            return pedidoDao.updateEstadoPedido(idPedido, estado);
        }, executor);
    }

    // Eliminar pedido por ID
    public CompletableFuture<Integer> deletePedidoById(long idPedido) {
        return CompletableFuture.supplyAsync(() -> {
            return pedidoDao.deletePedidoById(idPedido);
        }, executor);
    }

    // Obtener pedidos por rango de fechas
    public LiveData<List<Pedido>> getPedidosByRangoFechas(Date fechaInicio, Date fechaFin) {
        return pedidoDao.getPedidosByRangoFechas(fechaInicio, fechaFin);
    }

    public LiveData<List<Pedido>> getPedidosByUsuario(int idUsuario) {
        return pedidoDao.getPedidosByUsuario(idUsuario);
    }
}