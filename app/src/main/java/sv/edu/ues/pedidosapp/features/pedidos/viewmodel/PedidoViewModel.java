// sv/edu/ues/pedidosapp/features/pedidos/viewmodel/PedidoViewModel.java
package sv.edu.ues.pedidosapp.features.pedidos.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import sv.edu.ues.pedidosapp.data.local.entity.Pedido;
import sv.edu.ues.pedidosapp.data.local.entity.DetallePedido;
import sv.edu.ues.pedidosapp.data.repository.PedidoRepository;
import sv.edu.ues.pedidosapp.features.core.BaseViewModel;

public class PedidoViewModel extends BaseViewModel {

    private PedidoRepository pedidoRepository;
    private LiveData<List<Pedido>> allPedidos;
    private MutableLiveData<PedidoResult> pedidoResult = new MutableLiveData<>();

    public PedidoViewModel(@NonNull Application application) {
        super(application);
        pedidoRepository = repositoryManager.getPedidoRepository();
        allPedidos = pedidoRepository.getAllPedidos();
    }

    // LiveData para observar todos los pedidos
    public LiveData<List<Pedido>> getAllPedidos() {
        return allPedidos;
    }

    // LiveData para observar el resultado de las operaciones
    public LiveData<PedidoResult> getPedidoResult() {
        return pedidoResult;
    }

    // Obtener pedidos por usuario
    public LiveData<List<Pedido>> getPedidosByUsuario(int idUsuario) {
        return pedidoRepository.getPedidosByUsuario(idUsuario);
    }

    // Obtener pedido por ID
    public LiveData<Pedido> getPedidoById(int idPedido) {
        return pedidoRepository.getPedidoById(idPedido);
    }

    // Obtener pedidos por estado
    public LiveData<List<Pedido>> getPedidosByEstado(String estado) {
        return pedidoRepository.getPedidosByEstado(estado);
    }

    // Obtener pedidos por usuario y estado
    public LiveData<List<Pedido>> getPedidosByUsuarioAndEstado(int idUsuario, String estado) {
        return pedidoRepository.getPedidosByUsuarioAndEstado(idUsuario, estado);
    }

    // Insertar pedido completo
    public void insertPedidoCompleto(Pedido pedido, List<DetallePedido> detalles) {
        pedidoRepository.insertPedidoCompleto(pedido, detalles)
                .thenAccept(result -> {
                    pedidoResult.postValue(new PedidoResult(true, "Pedido creado exitosamente", result.getPedidoId()));
                })
                .exceptionally(e -> {
                    pedidoResult.postValue(new PedidoResult(false, "Error: " + e.getMessage(), -1));
                    return null;
                });
    }

    // Actualizar pedido
    public void updatePedido(Pedido pedido) {
        pedidoRepository.updatePedido(pedido)
                .thenAccept(result -> {
                    pedidoResult.postValue(new PedidoResult(result > 0, "Pedido actualizado exitosamente", pedido.getIdPedido()));
                })
                .exceptionally(e -> {
                    pedidoResult.postValue(new PedidoResult(false, "Error: " + e.getMessage(), -1));
                    return null;
                });
    }

    // Actualizar estado del pedido
    public void updateEstadoPedido(int idPedido, String estado) {
        pedidoRepository.updateEstadoPedido(idPedido, estado)
                .thenAccept(result -> {
                    pedidoResult.postValue(new PedidoResult(result > 0, "Estado actualizado exitosamente", idPedido));
                })
                .exceptionally(e -> {
                    pedidoResult.postValue(new PedidoResult(false, "Error: " + e.getMessage(), -1));
                    return null;
                });
    }

    // Eliminar pedido completo
    public void deletePedidoCompleto(int idPedido) {
        pedidoRepository.deletePedidoCompleto(idPedido)
                .thenAccept(result -> {
                    pedidoResult.postValue(new PedidoResult(result, "Pedido eliminado exitosamente", idPedido));
                })
                .exceptionally(e -> {
                    pedidoResult.postValue(new PedidoResult(false, "Error: " + e.getMessage(), -1));
                    return null;
                });
    }

    // Obtener pedidos por rango de fechas
    public LiveData<List<Pedido>> getPedidosByFechaRange(long fechaInicio, long fechaFin) {
        return pedidoRepository.getPedidosByFechaRange(fechaInicio, fechaFin);
    }

    // Obtener total de ventas por usuario
    public LiveData<Double> getTotalVentasByUsuario(int idUsuario) {
        return pedidoRepository.getTotalVentasByUsuario(idUsuario);
    }

    // Contar pedidos por estado
    public LiveData<Integer> countPedidosByEstado(String estado) {
        return pedidoRepository.countPedidosByEstado(estado);
    }

    // Obtener últimos pedidos
    public LiveData<List<Pedido>> getUltimosPedidos(int limite) {
        return pedidoRepository.getUltimosPedidos(limite);
    }

    // Clase para encapsular el resultado de las operaciones
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