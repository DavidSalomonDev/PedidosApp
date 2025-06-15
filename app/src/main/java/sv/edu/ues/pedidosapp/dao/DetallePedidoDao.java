package sv.edu.ues.pedidosapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.edu.ues.pedidosapp.entity.DetallePedido;
import sv.edu.ues.pedidosapp.entity.DetallePedidoConProducto;

// DetallePedidoDao.java
@Dao
public interface DetallePedidoDao {

    @Query("SELECT * FROM detalle_pedidos WHERE pedido_id = :pedidoId")
    LiveData<List<DetallePedido>> getDetallesByPedido(int pedidoId);

    @Query("SELECT dp.*, p.nombre as producto_nombre, p.imagen_url " +
            "FROM detalle_pedidos dp " +
            "INNER JOIN productos p ON dp.producto_id = p.id " +
            "WHERE dp.pedido_id = :pedidoId")
    LiveData<List<DetallePedidoConProducto>> getDetallesConProducto(int pedidoId);

    @Insert
    void insertDetalle(DetallePedido detalle);

    @Insert
    void insertDetalles(List<DetallePedido> detalles);

    @Update
    void updateDetalle(DetallePedido detalle);

    @Delete
    void deleteDetalle(DetallePedido detalle);

    @Query("DELETE FROM detalle_pedidos WHERE pedido_id = :pedidoId")
    void deleteDetallesByPedido(int pedidoId);
}