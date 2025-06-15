package sv.edu.ues.pedidosapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.edu.ues.pedidosapp.entity.Pedido;

// PedidoDao.java
@Dao
public interface PedidoDao {

    @Query("SELECT * FROM pedidos ORDER BY fecha DESC")
    LiveData<List<Pedido>> getAllPedidos();

    @Query("SELECT * FROM pedidos WHERE usuario_id = :usuarioId ORDER BY fecha DESC")
    LiveData<List<Pedido>> getPedidosByUsuario(int usuarioId);

    @Query("SELECT * FROM pedidos WHERE id = :id")
    LiveData<Pedido> getPedidoById(int id);

    @Query("SELECT * FROM pedidos WHERE estado = :estado")
    LiveData<List<Pedido>> getPedidosByEstado(String estado);

    @Insert
    long insertPedido(Pedido pedido);

    @Update
    void updatePedido(Pedido pedido);

    @Delete
    void deletePedido(Pedido pedido);

    @Query("DELETE FROM pedidos WHERE id = :id")
    void deletePedidoById(int id);
}