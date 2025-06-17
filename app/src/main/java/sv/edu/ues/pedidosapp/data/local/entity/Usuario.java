package sv.edu.ues.pedidosapp.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_usuario")
    private int idUsuario;

    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "telefono")
    private String telefono;

    @ColumnInfo(name = "direccion")
    private String direccion;

    @ColumnInfo(name = "fecha_registro")
    private long fechaRegistro;

    // Constructor vacío (requerido por Room)
    public Usuario() {
    }

    // Constructor completo
    public Usuario(String nombre, String email, String password, String telefono, String direccion, long fechaRegistro) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
       // this.direccion = direccion;
       // this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public long getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(long fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}