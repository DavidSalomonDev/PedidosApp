package sv.edu.ues.pedidosapp.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "pedidos",
        foreignKeys = @ForeignKey(entity = Usuario.class,
                parentColumns = "id_usuario",
                childColumns = "id_usuario",
                onDelete = ForeignKey.CASCADE))
public class Pedido {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_pedido")
    private int idPedido;

    @ColumnInfo(name = "id_usuario")
    private int idUsuario;

    @ColumnInfo(name = "fecha_pedido")
    private long fechaPedido;

    @ColumnInfo(name = "estado")
    private String estado; // "PENDIENTE", "CONFIRMADO", "EN_PREPARACION", "ENTREGADO", "CANCELADO"

    @ColumnInfo(name = "total")
    private double total;

    @ColumnInfo(name = "direccion_entrega")
    private String direccionEntrega;

    @ColumnInfo(name = "notas")
    private String notas;

    // Constructor vacío
    public Pedido() {
    }

    // Constructor completo
    public Pedido(int idUsuario, long fechaPedido, String estado, double total, String direccionEntrega, String notas) {
        this.idUsuario = idUsuario;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.total = total;
        this.direccionEntrega = direccionEntrega;
        this.notas = notas;
    }

    // Getters y Setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public long getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(long fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}