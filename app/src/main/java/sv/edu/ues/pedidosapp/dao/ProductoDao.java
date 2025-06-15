package sv.edu.ues.pedidosapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.edu.ues.pedidosapp.entity.Producto;

@Dao
public interface ProductoDao {
    @Query("SELECT * FROM productos")
    List<Producto> getAll();

    @Query("SELECT * FROM productos WHERE id = :id")
    Producto getById(int id);

    @Query("SELECT * FROM productos WHERE categoria = :categoria")
    List<Producto> getByCategoria(String categoria);

    @Insert
    void insertAll(Producto... productos);

    @Update
    void update(Producto producto);

    @Delete
    void delete(Producto producto);
}