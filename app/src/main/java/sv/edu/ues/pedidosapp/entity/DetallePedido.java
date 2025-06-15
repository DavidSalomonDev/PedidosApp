package sv.edu.ues.pedidosapp.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

// DetallePedido.java
@Entity(tableName = "detalle_pedidos",
        foreignKeys = {
                @ForeignKey(entity = Pedido.class, parentColumns = "id", childColumns = "pedido_id"),
                @ForeignKey(entity = Producto.class, parentColumns = "id", childColumns = "producto_id")
        })
public class DetallePedido {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "pedido_id")
    private int pedidoId;

    @ColumnInfo(name = "producto_id")
    private int productoId;

    @ColumnInfo(name = "cantidad")
    private int cantidad;

    @ColumnInfo(name = "precio_unitario")
    private double precioUnitario;

    @ColumnInfo(name = "subtotal")
    private double subtotal;

    // Constructores, getters y setters
    public DetallePedido() {
    }

    public DetallePedido(int pedidoId, int productoId, int cantidad, double precioUnitario) {
        this.pedidoId = pedidoId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }

    // Getters y setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
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
}