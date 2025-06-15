package sv.edu.ues.pedidosapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UsuarioDao {
    @Query("SELECT * FROM usuarios")
    List<Usuario> getAll();

    @Query("SELECT * FROM usuarios WHERE id = :id")
    Usuario getById(int id);

    @Query("SELECT * FROM usuarios WHERE email = :email")
    Usuario getByEmail(String email);

    @Insert
    void insertAll(Usuario... usuarios);

    @Update
    void update(Usuario usuario);

    @Delete
    void delete(Usuario usuario);
}