package sv.edu.ues.pedidosapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mesas")
public class Mesa {
    @PrimaryKey
    private int numero;
    private int capacidad;
    private String estado; // "libre", "ocupada", "reservada"
    private String ubicacion; // "terraza", "interior", "vip"

    // Constructor vacío
    public Mesa() {}

    // Constructor con parámetros
    public Mesa(int numero, int capacidad, String ubicacion) {
        this.numero = numero;
        this.capacidad = capacidad;
        this.ubicacion = ubicacion;
        this.estado = "libre";
    }

    // Getters y Setters
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}