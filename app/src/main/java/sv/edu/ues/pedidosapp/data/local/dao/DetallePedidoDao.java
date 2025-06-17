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

    // Insertar múltiples detalles
    @Insert
    void insertDetallesPedido(List<DetallePedido> detallesPedido);

    // Actualizar detalle de pedido
    @Update
    int updateDetallePedido(DetallePedido detallePedido);

    // Eliminar detalle de pedido
    @Delete
    int deleteDetallePedido(DetallePedido detallePedido);

    // Obtener todos los detalles de un pedido
    @Query("SELECT * FROM detalle_pedidos WHERE id_pedido = :idPedido")
    LiveData<List<DetallePedido>> getDetallesByPedido(int idPedido);

    // Obtener detalles de un pedido (síncrono)
    @Query("SELECT * FROM detalle_pedidos WHERE id_pedido = :idPedido")
    List<DetallePedido> getDetallesByPedidoSync(int idPedido);

    // Obtener detalle por ID
    @Query("SELECT * FROM detalle_pedidos WHERE id_detalle = :idDetalle")
    LiveData<DetallePedido> getDetalleById(int idDetalle);

    // Obtener detalle por ID (síncrono)
    @Query("SELECT * FROM detalle_pedidos WHERE id_detalle = :idDetalle")
    DetallePedido getDetalleByIdSync(int idDetalle);

    // Obtener detalles con información del producto (JOIN)
    @Query("SELECT dp.*, p.nombre as nombre_producto, p.imagen_url " +
            "FROM detalle_pedidos dp " +
            "INNER JOIN productos p ON dp.id_producto = p.id_producto " +
            "WHERE dp.id_pedido = :idPedido")
    LiveData<List<DetallePedidoConProducto>> getDetallesConProducto(int idPedido);

    // Actualizar cantidad de un detalle
    @Query("UPDATE detalle_pedidos SET cantidad = :cantidad, subtotal = :subtotal WHERE id_detalle = :idDetalle")
    int updateCantidadDetalle(int idDetalle, int cantidad, double subtotal);

    // Eliminar todos los detalles de un pedido
    @Query("DELETE FROM detalle_pedidos WHERE id_pedido = :idPedido")
    int deleteDetallesByPedido(int idPedido);

    // Obtener cantidad total de productos en un pedido
    @Query("SELECT SUM(cantidad) FROM detalle_pedidos WHERE id_pedido = :idPedido")
    LiveData<Integer> getTotalCantidadByPedido(int idPedido);

    // Obtener productos más vendidos
    @Query("SELECT id_producto, SUM(cantidad) as total_vendido " +
            "FROM detalle_pedidos " +
            "GROUP BY id_producto " +
            "ORDER BY total_vendido DESC " +
            "LIMIT :limite")
    LiveData<List<ProductoVendido>> getProductosMasVendidos(int limite);

    // Verificar si un producto está en algún pedido
    @Query("SELECT COUNT(*) FROM detalle_pedidos WHERE id_producto = :idProducto")
    int countDetallesByProducto(int idProducto);

    // Clase para el resultado del JOIN
    public static class DetallePedidoConProducto {
        public int idDetalle;
        public int idPedido;
        public int idProducto;
        public int cantidad;
        public double precioUnitario;
        public double subtotal;
        public String nombreProducto;
        public String imagenUrl;

        // Getters y setters
        public int getIdDetalle() {
            return idDetalle;
        }

        public void setIdDetalle(int idDetalle) {
            this.idDetalle = idDetalle;
        }

        public int getIdPedido() {
            return idPedido;
        }

        public void setIdPedido(int idPedido) {
            this.idPedido = idPedido;
        }

        public int getIdProducto() {
            return idProducto;
        }

        public void setIdProducto(int idProducto) {
            this.idProducto = idProducto;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }

        public double getPrecioUnitario() {
            return precioUnitario;
        }

        public void setPrecioUnitario(double precioUnitario) {
            this.precioUnitario = precioUnitario;
        }

        public double getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(double subtotal) {
            this.subtotal = subtotal;
        }

        public String getNombreProducto() {
            return nombreProducto;
        }

        public void setNombreProducto(String nombreProducto) {
            this.nombreProducto = nombreProducto;
        }

        public String getImagenUrl() {
            return imagenUrl;
        }

        public void setImagenUrl(String imagenUrl) {
            this.imagenUrl = imagenUrl;
        }
    }

    // Clase para productos más vendidos
    public static class ProductoVendido {
        public int idProducto;
        public int totalVendido;

        public int getIdProducto() {
            return idProducto;
        }

        public void setIdProducto(int idProducto) {
            this.idProducto = idProducto;
        }

        public int getTotalVendido() {
            return totalVendido;
        }

        public void setTotalVendido(int totalVendido) {
            this.totalVendido = totalVendido;
        }
    }
}