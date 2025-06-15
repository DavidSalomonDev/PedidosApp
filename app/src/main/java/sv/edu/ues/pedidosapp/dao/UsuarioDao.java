package sv.edu.ues.pedidosapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.edu.ues.pedidosapp.entity.Usuario;
@Dao
public interface UsuarioDao {

    @Query("SELECT * FROM usuarios ORDER BY nombre ASC")
    LiveData<List<Usuario>> getAllUsuarios();

    @Query("SELECT * FROM usuarios WHERE id = :id")
    LiveData<Usuario> getUsuarioById(int id);

    @Query("SELECT * FROM usuarios WHERE email = :email")
    LiveData<Usuario> getUsuarioByEmail(String email);

    @Insert
    long insertUsuario(Usuario usuario);

    @Update
    void updateUsuario(Usuario usuario);

    @Delete
    void deleteUsuario(Usuario usuario);

    @Query("DELETE FROM usuarios WHERE id = :id")
    void deleteUsuarioById(int id);
}