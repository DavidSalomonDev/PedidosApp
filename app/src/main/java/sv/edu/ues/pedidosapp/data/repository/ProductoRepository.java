package sv.edu.ues.pedidosapp.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import sv.edu.ues.pedidosapp.data.local.dao.ProductoDao;
import sv.edu.ues.pedidosapp.data.local.db.AppDatabase;
import sv.edu.ues.pedidosapp.data.local.entity.Producto;

public class ProductoRepository extends BaseRepository {

    private ProductoDao productoDao;
    private LiveData<List<Producto>> allProductos;
    private LiveData<List<Producto>> productosDisponibles;

    public ProductoRepository(Application application) {
        super(application);
        productoDao = database.productoDao();
        allProductos = productoDao.getAllProductos();
        productosDisponibles = productoDao.getProductosDisponibles();
    }

    // Obtener todos los productos
    public LiveData<List<Producto>> getAllProductos() {
        return allProductos;
    }

    // Obtener productos disponibles
    public LiveData<List<Producto>> getProductosDisponibles() {
        return productosDisponibles;
    }

    // Obtener producto por ID
    public LiveData<Producto> getProductoById(int idProducto) {
        return productoDao.getProductoById(idProducto);
    }

    // Obtener productos por categoría
    public LiveData<List<Producto>> getProductosByCategoria(String categoria) {
        return productoDao.getProductosByCategoria(categoria);
    }

    // Buscar productos por nombre
    public LiveData<List<Producto>> searchProductosByNombre(String nombre) {
        return productoDao.searchProductosByNombre(nombre);
    }

    // Obtener categorías
    public LiveData<List<String>> getCategorias() {
        return productoDao.getCategorias();
    }

    // Insertar producto
    public CompletableFuture<Long> insertProducto(Producto producto) {
        return CompletableFuture.supplyAsync(() -> {
            return productoDao.insertProducto(producto);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Insertar múltiples productos
    public CompletableFuture<Void> insertProductos(List<Producto> productos) {
        return CompletableFuture.runAsync(() -> {
            productoDao.insertProductos(productos);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Actualizar producto
    public CompletableFuture<Integer> updateProducto(Producto producto) {
        return CompletableFuture.supplyAsync(() -> {
            return productoDao.updateProducto(producto);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Eliminar producto
    public CompletableFuture<Integer> deleteProducto(Producto producto) {
        return CompletableFuture.supplyAsync(() -> {
            return productoDao.deleteProducto(producto);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Cambiar disponibilidad
    public CompletableFuture<Integer> updateDisponibilidad(int idProducto, boolean disponible) {
        return CompletableFuture.supplyAsync(() -> {
            return productoDao.updateDisponibilidad(idProducto, disponible);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Obtener producto por ID (síncrono)
    public CompletableFuture<Producto> getProductoByIdSync(int idProducto) {
        return CompletableFuture.supplyAsync(() -> {
            return productoDao.getProductoByIdSync(idProducto);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Contar productos por categoría
    public CompletableFuture<Integer> countProductosByCategoria(String categoria) {
        return CompletableFuture.supplyAsync(() -> {
            return productoDao.countProductosByCategoria(categoria);
        }, AppDatabase.databaseWriteExecutor);
    }
}