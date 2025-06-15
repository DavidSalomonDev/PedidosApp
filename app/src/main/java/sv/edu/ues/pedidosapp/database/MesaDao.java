package sv.edu.ues.pedidosapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MesaDao {
    @Query("SELECT * FROM mesas")
    List<Mesa> getAll();

    @Query("SELECT * FROM mesas WHERE numero = :numero")
    Mesa getByNumero(int numero);

    @Query("SELECT * FROM mesas WHERE ubicacion = :ubicacion")
    List<Mesa> getByUbicacion(String ubicacion);

    @Insert
    void insertAll(Mesa... mesas);

    @Update
    void update(Mesa mesa);

    @Delete
    void delete(Mesa mesa);
}