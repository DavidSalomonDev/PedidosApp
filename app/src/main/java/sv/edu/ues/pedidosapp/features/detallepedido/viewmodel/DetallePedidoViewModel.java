// sv/edu/ues/pedidosapp/features/detallepedido/viewmodel/DetallePedidoViewModel.java
package sv.edu.ues.pedidosapp.features.detallepedido.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import sv.edu.ues.pedidosapp.data.local.entity.DetallePedido;
import sv.edu.ues.pedidosapp.data.repository.DetallePedidoRepository;

public class DetallePedidoViewModel extends AndroidViewModel {

    private DetallePedidoRepository detallePedidoRepository;
    private MutableLiveData<String> operationResult = new MutableLiveData<>();

    public DetallePedidoViewModel(@NonNull Application application) {
        super(application);
        detallePedidoRepository = new DetallePedidoRepository(application);
    }

    // Insertar detalle de pedido
    public void insertDetallePedido(DetallePedido detallePedido) {
        detallePedidoRepository.insertDetallePedido(detallePedido).thenAccept(result -> {
            if (result > 0) {
                operationResult.postValue("Detalle de pedido creado exitosamente");
            } else {
                operationResult.postValue("Error al crear el detalle de pedido");
            }
        }).exceptionally(throwable -> {
            operationResult.postValue("Error: " + throwable.getMessage());
            return null;
        });
    }

    // Actualizar detalle de pedido
    public void updateDetallePedido(DetallePedido detallePedido) {
        detallePedidoRepository.updateDetallePedido(detallePedido).thenAccept(result -> {
            if (result > 0) {
                operationResult.postValue("Detalle de pedido actualizado exitosamente");
            } else {
                operationResult.postValue("Error al actualizar el detalle de pedido");
            }
        }).exceptionally(throwable -> {
            operationResult.postValue("Error: " + throwable.getMessage());
            return null;
        });
    }

    // Eliminar detalle de pedido
    public void deleteDetallePedido(DetallePedido detallePedido) {
        detallePedidoRepository.deleteDetallePedido(detallePedido).thenAccept(result -> {
            if (result > 0) {
                operationResult.postValue("Detalle de pedido eliminado exitosamente");
            } else {
                operationResult.postValue("Error al eliminar el detalle de pedido");
            }
        }).exceptionally(throwable -> {
            operationResult.postValue("Error: " + throwable.getMessage());
            return null;
        });
    }

    // Obtener detalles de un pedido específico
    public LiveData<List<DetallePedido>> getDetallesByPedido(int idPedido) {
        return detallePedidoRepository.getDetallesByPedido(idPedido);
    }

    // Obtener detalle por ID
    public LiveData<DetallePedido> getDetalleById(int idDetalle) {
        return detallePedidoRepository.getDetalleById(idDetalle);
    }

    // Eliminar todos los detalles de un pedido
    public void deleteDetallesByPedido(int idPedido) {
        detallePedidoRepository.deleteDetallesByPedido(idPedido).thenAccept(result -> {
            if (result > 0) {
                operationResult.postValue("Detalles del pedido eliminados exitosamente");
            } else {
                operationResult.postValue("Error al eliminar los detalles del pedido");
            }
        }).exceptionally(throwable -> {
            operationResult.postValue("Error: " + throwable.getMessage());
            return null;
        });
    }

    // Obtener la suma total de un pedido
    public LiveData<Double> getTotalPedido(int idPedido) {
        return detallePedidoRepository.getTotalPedido(idPedido);
    }

    // Obtener resultado de operaciones
    public LiveData<String> getOperationResult() {
        return operationResult;
    }
}