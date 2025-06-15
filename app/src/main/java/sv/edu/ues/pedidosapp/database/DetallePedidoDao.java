package sv.edu.ues.pedidosapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DetallePedidoDao {
    @Query("SELECT * FROM detalle_pedidos")
    List<DetallePedido> getAll();

    @Query("SELECT * FROM detalle_pedidos WHERE id = :id")
    DetallePedido getById(int id);

    @Query("SELECT * FROM detalle_pedidos WHERE pedido_id = :pedidoId")
    List<DetallePedido> getByPedidoId(int pedidoId);

    @Insert
    void insertAll(DetallePedido... detalles);

    @Update
    void update(DetallePedido detalle);

    @Delete
    void delete(DetallePedido detalle);
}