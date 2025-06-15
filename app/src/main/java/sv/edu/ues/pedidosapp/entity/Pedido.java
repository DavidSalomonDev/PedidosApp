package sv.edu.ues.pedidosapp.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Pedido.java
@Entity(tableName = "pedidos")
public class Pedido {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "usuario_id")
    private int usuarioId;

    @ColumnInfo(name = "fecha")
    private String fecha;

    @ColumnInfo(name = "total")
    private double total;

    @ColumnInfo(name = "estado")
    private String estado; // "pendiente", "preparando", "listo", "entregado"

    @ColumnInfo(name = "direccion_entrega")
    private String direccionEntrega;

    @ColumnInfo(name = "notas")
    private String notas;

    // Constructores, getters y setters
    public Pedido() {}

    public Pedido(int usuarioId, String fecha, double total, String estado, String direccionEntrega, String notas) {
        this.usuarioId = usuarioId;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
        this.direccionEntrega = direccionEntrega;
        this.notas = notas;
    }

    // Getters y setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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