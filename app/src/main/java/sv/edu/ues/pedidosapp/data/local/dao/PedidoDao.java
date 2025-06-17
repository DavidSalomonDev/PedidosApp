package sv.edu.ues.pedidosapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.edu.ues.pedidosapp.data.local.entity.Pedido;

@Dao
public interface PedidoDao {

    // Insertar pedido
    @Insert
    long insertPedido(Pedido pedido);

    // Actualizar pedido
    @Update
    int updatePedido(Pedido pedido);

    // Eliminar pedido
    @Delete
    int deletePedido(Pedido pedido);

    // Obtener todos los pedidos
    @Query("SELECT * FROM pedidos ORDER BY fecha_pedido DESC")
    LiveData<List<Pedido>> getAllPedidos();

    // Obtener pedidos por usuario
    @Query("SELECT * FROM pedidos WHERE id_usuario = :idUsuario ORDER BY fecha_pedido DESC")
    LiveData<List<Pedido>> getPedidosByUsuario(int idUsuario);

    // Obtener pedido por ID
    @Query("SELECT * FROM pedidos WHERE id_pedido = :idPedido")
    LiveData<Pedido> getPedidoById(int idPedido);

    // Obtener pedido por ID (síncrono)
    @Query("SELECT * FROM pedidos WHERE id_pedido = :idPedido")
    Pedido getPedidoByIdSync(int idPedido);

    // Obtener pedidos por estado
    @Query("SELECT * FROM pedidos WHERE estado = :estado ORDER BY fecha_pedido DESC")
    LiveData<List<Pedido>> getPedidosByEstado(String estado);

    // Obtener pedidos por usuario y estado
    @Query("SELECT * FROM pedidos WHERE id_usuario = :idUsuario AND estado = :estado ORDER BY fecha_pedido DESC")
    LiveData<List<Pedido>> getPedidosByUsuarioAndEstado(int idUsuario, String estado);

    // Actualizar estado del pedido
    @Query("UPDATE pedidos SET estado = :estado WHERE id_pedido = :idPedido")
    int updateEstadoPedido(int idPedido, String estado);

    // Obtener pedidos por rango de fechas
    @Query("SELECT * FROM pedidos WHERE fecha_pedido BETWEEN :fechaInicio AND :fechaFin ORDER BY fecha_pedido DESC")
    LiveData<List<Pedido>> getPedidosByFechaRange(long fechaInicio, long fechaFin);

    // Obtener total de ventas por usuario
    @Query("SELECT SUM(total) FROM pedidos WHERE id_usuario = :idUsuario AND estado != 'CANCELADO'")
    LiveData<Double> getTotalVentasByUsuario(int idUsuario);

    // Contar pedidos por estado
    @Query("SELECT COUNT(*) FROM pedidos WHERE estado = :estado")
    LiveData<Integer> countPedidosByEstado(String estado);

    // Eliminar pedido por ID
    @Query("DELETE FROM pedidos WHERE id_pedido = :idPedido")
    int deletePedidoById(int idPedido);

    // Obtener últimos pedidos (límite)
    @Query("SELECT * FROM pedidos ORDER BY fecha_pedido DESC LIMIT :limite")
    LiveData<List<Pedido>> getUltimosPedidos(int limite);
}