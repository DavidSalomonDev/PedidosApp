package sv.edu.ues.pedidosapp.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "detalle_pedidos",
        indices = {
                @Index(value = "id_detalle"),
                @Index(value = "id_pedido"),
                @Index(value = "id_producto")
        },
        foreignKeys = {
                @ForeignKey(entity = Pedido.class,
                        parentColumns = "id_pedido",
                        childColumns = "id_pedido",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Producto.class,
                        parentColumns = "id_producto",
                        childColumns = "id_producto",
                        onDelete = ForeignKey.CASCADE),
        })
public class DetallePedido {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_detalle")
    private int idDetalle;

    @ColumnInfo(name = "id_pedido")
    private int idPedido;

    @ColumnInfo(name = "id_producto")
    private int idProducto;

    @ColumnInfo(name = "cantidad")
    private int cantidad;

    @ColumnInfo(name = "precio_unitario")
    private double precioUnitario;

    @ColumnInfo(name = "subtotal")
    private double subtotal;

    // Constructor vacío
    public DetallePedido() {
    }

    // Constructor con parámetros
    @Ignore
    public DetallePedido(int idPedido, int idProducto, int cantidad, double precioUnitario) {
        this.idPedido = idPedido;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }

    // Getters y Setters
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
        this.subtotal = cantidad * precioUnitario; // Recalcular subtotal
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario; // Recalcular subtotal
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}