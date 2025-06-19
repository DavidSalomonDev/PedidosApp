package sv.edu.ues.pedidosapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.edu.ues.pedidosapp.data.local.entity.Producto;

@Dao
public interface ProductoDao {

    // ========== OPERACIONES CRUD ==========

    // Insertar producto
    @Insert
    long insertProducto(Producto producto);

    // Actualizar producto
    @Update
    int updateProducto(Producto producto);

    // Eliminar producto
    @Delete
    int deleteProducto(Producto producto);

    // Eliminar producto por ID
    @Query("DELETE FROM productos WHERE id_producto = :idProducto")
    int deleteProductoById(int idProducto);

    // ========== CONSULTAS INDIVIDUALES ==========

    // Obtener producto por ID
    @Query("SELECT * FROM productos WHERE id_producto = :idProducto")
    LiveData<Producto> getProductoById(int idProducto);

    // Obtener producto por ID (síncrono)
    @Query("SELECT * FROM productos WHERE id_producto = :idProducto")
    Producto getProductoByIdSync(int idProducto);

    // ========== CONSULTAS CON PAGINACIÓN ==========

    // Obtener todos los productos (Paginado)
    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    PagingSource<Integer, Producto> getAllProductosPagingSource();

    // Obtener productos por categoría (Paginado)
    @Query("SELECT * FROM productos WHERE categoria = :categoria ORDER BY nombre ASC")
    PagingSource<Integer, Producto> getProductosByCategoriaPagingSource(String categoria);

    // Obtener productos disponibles (Paginado)
    @Query("SELECT * FROM productos WHERE disponible = 1 ORDER BY nombre ASC")
    PagingSource<Integer, Producto> getProductosDisponiblesPagingSource();

    // Buscar productos por nombre (Paginado)
    @Query("SELECT * FROM productos WHERE nombre LIKE '%' || :nombre || '%' ORDER BY nombre ASC")
    PagingSource<Integer, Producto> buscarProductosPorNombrePagingSource(String nombre);

    // ========== CONSULTAS SIN PAGINACIÓN (para casos específicos) ==========

    // Obtener todos los productos (sin paginación)
    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    LiveData<List<Producto>> getAllProductos();

    // Obtener productos por categoría (sin paginación)
    @Query("SELECT * FROM productos WHERE categoria = :categoria ORDER BY nombre ASC")
    LiveData<List<Producto>> getProductosByCategoria(String categoria);

    // Obtener productos disponibles (sin paginación)
    @Query("SELECT * FROM productos WHERE disponible = 1 ORDER BY nombre ASC")
    LiveData<List<Producto>> getProductosDisponibles();

    // Buscar productos por nombre (sin paginación)
    @Query("SELECT * FROM productos WHERE nombre LIKE '%' || :nombre || '%' ORDER BY nombre ASC")
    LiveData<List<Producto>> buscarProductosPorNombre(String nombre);

    // ========== OPERACIONES DE ACTUALIZACIÓN ESPECÍFICAS ==========

    // Actualizar disponibilidad del producto
    @Query("UPDATE productos SET disponible = :disponible WHERE id_producto = :idProducto")
    int updateDisponibilidadProducto(int idProducto, boolean disponible);

    // Actualizar precio del producto
    @Query("UPDATE productos SET precio = :precio WHERE id_producto = :idProducto")
    int updatePrecioProducto(int idProducto, double precio);

    // ========== CONSULTAS DE UTILIDAD ==========

    // Contar productos
    @Query("SELECT COUNT(*) FROM productos")
    int getProductoCount();

    // Contar productos por categoría
    @Query("SELECT COUNT(*) FROM productos WHERE categoria = :categoria")
    int getProductoCountByCategoria(String categoria);

    // Contar productos disponibles
    @Query("SELECT COUNT(*) FROM productos WHERE disponible = 1")
    int getProductosDisponiblesCount();

    // Obtener todas las categorías únicas
    @Query("SELECT DISTINCT categoria FROM productos ORDER BY categoria ASC")
    LiveData<List<String>> getAllCategorias();

    // ========== OPERACIONES DE LIMPIEZA ==========

    // Eliminar todos los productos
    @Query("DELETE FROM productos")
    void deleteAllProductos();

    // Eliminar productos por categoría
    @Query("DELETE FROM productos WHERE categoria = :categoria")
    int deleteProductosByCategoria(String categoria);
}