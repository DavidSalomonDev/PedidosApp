package sv.edu.ues.pedidosapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "pedidos")
public class Pedido {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "fecha")
    private String fecha;

    @ColumnInfo(name = "mesa_numero")
    private int mesaNumero;

    @ColumnInfo(name = "usuario_id")
    private int usuarioId;

    @ColumnInfo(name = "estado")
    private String estado; // Ejemplo: "pendiente", "servido", "pagado"

    // Constructor vacío
    public Pedido() {}

    // Constructor con parámetros
    public Pedido(String fecha, int mesaNumero, int usuarioId, String estado) {
        this.fecha = fecha;
        this.mesaNumero = mesaNumero;
        this.usuarioId = usuarioId;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public int getMesaNumero() { return mesaNumero; }
    public void setMesaNumero(int mesaNumero) { this.mesaNumero = mesaNumero; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}