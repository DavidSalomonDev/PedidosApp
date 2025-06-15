package sv.edu.ues.pedidosapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PedidoDao {
    @Query("SELECT * FROM pedidos")
    List<Pedido> getAll();

    @Query("SELECT * FROM pedidos WHERE id = :id")
    Pedido getById(int id);

    @Query("SELECT * FROM pedidos WHERE mesa_numero = :mesaNumero")
    List<Pedido> getByMesaNumero(int mesaNumero);

    @Query("SELECT * FROM pedidos WHERE usuario_id = :usuarioId")
    List<Pedido> getByUsuarioId(int usuarioId);

    @Insert
    long insertAll(Pedido pedidos);

    @Update
    void update(Pedido pedido);

    @Delete
    void delete(Pedido pedido);
}