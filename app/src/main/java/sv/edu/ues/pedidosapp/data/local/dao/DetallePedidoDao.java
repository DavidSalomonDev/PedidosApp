package sv.edu.ues.pedidosapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.edu.ues.pedidosapp.data.local.entity.DetallePedido;

@Dao
public interface DetallePedidoDao {

    // Insertar detalle de pedido
    @Insert
    long insertDetallePedido(DetallePedido detallePedido);

    // Actualizar detalle de pedido
    @Update
    int updateDetallePedido(DetallePedido detallePedido);

    // Eliminar detalle de pedido
    @Delete
    int deleteDetallePedido(DetallePedido detallePedido);

    // Obtener detalles de un pedido específico
    @Query("SELECT * FROM detalle_pedidos WHERE id_pedido = :idPedido")
    LiveData<List<DetallePedido>> getDetallesByPedido(int idPedido);

    // Obtener detalle por ID
    @Query("SELECT * FROM detalle_pedidos WHERE id_detalle = :idDetalle")
    LiveData<DetallePedido> getDetalleById(int idDetalle);

    // Eliminar todos los detalles de un pedido
    @Query("DELETE FROM detalle_pedidos WHERE id_pedido = :idPedido")
    int deleteDetallesByPedido(int idPedido);

    // Obtener la suma total de un pedido
    @Query("SELECT SUM(subtotal) FROM detalle_pedidos WHERE id_pedido = :idPedido")
    LiveData<Double> getTotalPedido(int idPedido);

    @Query("SELECT COUNT(*) FROM detalle_pedidos")
    int getDetallePedidoCount();

    @Query("DELETE FROM detalle_pedidos")
    void deleteAllDetallesPedidos();

}