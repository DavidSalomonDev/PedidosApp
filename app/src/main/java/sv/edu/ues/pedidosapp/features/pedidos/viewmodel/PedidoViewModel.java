package sv.edu.ues.pedidosapp.features.pedidos.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import sv.edu.ues.pedidosapp.data.local.entity.Pedido;
import sv.edu.ues.pedidosapp.data.repository.PedidoRepository;

public class PedidoViewModel extends AndroidViewModel {

    private final PedidoRepository pedidoRepository;
    private final MutableLiveData<String> operationResult = new MutableLiveData<>();

    public PedidoViewModel(@NonNull Application application) {
        super(application);
        pedidoRepository = new PedidoRepository(application);
    }

    // Obtener todos los pedidos
    public LiveData<List<Pedido>> getAllPedidos() {
        return pedidoRepository.getAllPedidos();
    }

    // Obtener pedidos por usuario
    public LiveData<List<Pedido>> getPedidosByUsuario(int idUsuario) {
        return pedidoRepository.getPedidosByUsuario(idUsuario);
    }

    // Obtener pedido por ID
    public LiveData<Pedido> getPedidoById(long idPedido) {
        return pedidoRepository.getPedidoById(idPedido);
    }

    // Obtener pedidos por estado
    public LiveData<List<Pedido>> getPedidosByEstado(String estado) {
        return pedidoRepository.getPedidosByEstado(estado);
    }

    public CompletableFuture<Long> insertPedidoAndGetId(Pedido pedido) {
        return pedidoRepository.insertPedido(pedido);
    }

    // Insertar pedido
    public void insertPedido(Pedido pedido) {
        pedidoRepository.insertPedido(pedido)
                .thenAccept(result -> {
                    if (result > 0) {
                        operationResult.postValue("Pedido creado exitosamente");
                    } else {
                        operationResult.postValue("Error al crear el pedido");
                    }
                })
                .exceptionally(throwable -> {
                    operationResult.postValue("Error: " + throwable.getMessage());
                    return null;
                });
    }

    // Actualizar pedido
    public void updatePedido(Pedido pedido) {
        pedidoRepository.updatePedido(pedido)
                .thenAccept(result -> {
                    if (result > 0) {
                        operationResult.postValue("Pedido actualizado exitosamente");
                    } else {
                        operationResult.postValue("Error al actualizar el pedido");
                    }
                })
                .exceptionally(throwable -> {
                    operationResult.postValue("Error: " + throwable.getMessage());
                    return null;
                });
    }

    // Eliminar pedido
    public void deletePedido(Pedido pedido) {
        pedidoRepository.deletePedido(pedido)
                .thenAccept(result -> {
                    if (result > 0) {
                        operationResult.postValue("Pedido eliminado exitosamente");
                    } else {
                        operationResult.postValue("Error al eliminar el pedido");
                    }
                })
                .exceptionally(throwable -> {
                    operationResult.postValue("Error: " + throwable.getMessage());
                    return null;
                });
    }

    // Eliminar pedido por ID
    public void deletePedidoById(long idPedido) {
        pedidoRepository.deletePedidoById(idPedido)
                .thenAccept(result -> {
                    if (result > 0) {
                        operationResult.postValue("Pedido eliminado exitosamente");
                    } else {
                        operationResult.postValue("Error al eliminar el pedido");
                    }
                })
                .exceptionally(throwable -> {
                    operationResult.postValue("Error: " + throwable.getMessage());
                    return null;
                });
    }

    // Actualizar estado del pedido
    public void updateEstadoPedido(long idPedido, String nuevoEstado) {
        pedidoRepository.updateEstadoPedido(idPedido, nuevoEstado)
                .thenAccept(result -> {
                    if (result > 0) {
                        operationResult.postValue("Estado del pedido actualizado exitosamente");
                    } else {
                        operationResult.postValue("Error al actualizar el estado del pedido");
                    }
                })
                .exceptionally(throwable -> {
                    operationResult.postValue("Error: " + throwable.getMessage());
                    return null;
                });
    }

    // Obtener resultado de operaciones
    public LiveData<String> getOperationResult() {
        return operationResult;
    }

    // Limpiar resultado de operaciones
    public void clearOperationResult() {
        operationResult.setValue(null);
    }
}