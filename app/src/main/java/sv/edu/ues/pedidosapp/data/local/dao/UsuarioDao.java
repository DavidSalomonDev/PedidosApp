package sv.edu.ues.pedidosapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.edu.ues.pedidosapp.data.local.entity.Usuario;

@Dao
public interface UsuarioDao {

    // Insertar usuario
    @Insert
    long insertUsuario(Usuario usuario);

    // Actualizar usuario
    @Update
    int updateUsuario(Usuario usuario);

    // Eliminar usuario
    @Delete
    int deleteUsuario(Usuario usuario);

    // Obtener todos los usuarios
    @Query("SELECT * FROM usuarios ORDER BY nombre ASC")
    LiveData<List<Usuario>> getAllUsuarios();

    // Obtener usuario por ID
    @Query("SELECT * FROM usuarios WHERE id_usuario = :idUsuario")
    LiveData<Usuario> getUsuarioById(int idUsuario);

    // Obtener usuario por email (para login)
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    Usuario getUsuarioByEmail(String email);

    // Verificar si existe un email
    @Query("SELECT COUNT(*) FROM usuarios WHERE email = :email")
    int checkEmailExists(String email);

    // Login - verificar credenciales
    @Query("SELECT * FROM usuarios WHERE (email = :input OR nombre = :input) AND password = :password LIMIT 1")
    Usuario login(String input, String password);

    // Obtener usuario por ID (síncrono)
    @Query("SELECT * FROM usuarios WHERE id_usuario = :idUsuario")
    Usuario getUsuarioByIdSync(int idUsuario);

    // Eliminar usuario por ID
    @Query("DELETE FROM usuarios WHERE id_usuario = :idUsuario")
    int deleteUsuarioById(int idUsuario);

    @Query("SELECT COUNT(*) FROM usuarios")
    int getUserCount();

    @Query("DELETE FROM usuarios")
    void deleteAllUsuarios();


}