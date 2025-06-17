package sv.edu.ues.pedidosapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.edu.ues.pedidosapp.data.local.entity.Producto;

@Dao
public interface ProductoDao {

    // Insertar producto
    @Insert
    long insertProducto(Producto producto);

    // Insertar múltiples productos
    @Insert
    void insertProductos(List<Producto> productos);

    // Actualizar producto
    @Update
    int updateProducto(Producto producto);

    // Eliminar producto
    @Delete
    int deleteProducto(Producto producto);

    // Obtener todos los productos
    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    LiveData<List<Producto>> getAllProductos();

    // Obtener productos disponibles
    @Query("SELECT * FROM productos WHERE disponible = 1 ORDER BY nombre ASC")
    LiveData<List<Producto>> getProductosDisponibles();

    // Obtener producto por ID
    @Query("SELECT * FROM productos WHERE id_producto = :idProducto")
    LiveData<Producto> getProductoById(int idProducto);

    // Obtener producto por ID (síncrono)
    @Query("SELECT * FROM productos WHERE id_producto = :idProducto")
    Producto getProductoByIdSync(int idProducto);

    // Obtener productos por categoría
    @Query("SELECT * FROM productos WHERE categoria = :categoria AND disponible = 1 ORDER BY nombre ASC")
    LiveData<List<Producto>> getProductosByCategoria(String categoria);

    // Buscar productos por nombre
    @Query("SELECT * FROM productos WHERE nombre LIKE '%' || :nombre || '%' AND disponible = 1 ORDER BY nombre ASC")
    LiveData<List<Producto>> searchProductosByNombre(String nombre);

    // Obtener categorías únicas
    @Query("SELECT DISTINCT categoria FROM productos WHERE disponible = 1 ORDER BY categoria ASC")
    LiveData<List<String>> getCategorias();

    // Cambiar disponibilidad de producto
    @Query("UPDATE productos SET disponible = :disponible WHERE id_producto = :idProducto")
    int updateDisponibilidad(int idProducto, boolean disponible);

    // Eliminar producto por ID
    @Query("DELETE FROM productos WHERE id_producto = :idProducto")
    int deleteProductoById(int idProducto);

    // Contar productos por categoría
    @Query("SELECT COUNT(*) FROM productos WHERE categoria = :categoria AND disponible = 1")
    int countProductosByCategoria(String categoria);
}