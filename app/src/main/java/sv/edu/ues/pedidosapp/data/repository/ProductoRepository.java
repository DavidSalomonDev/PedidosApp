package sv.edu.ues.pedidosapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import sv.edu.ues.pedidosapp.data.local.AppDatabase;
import sv.edu.ues.pedidosapp.data.local.dao.ProductoDao;
import sv.edu.ues.pedidosapp.data.local.entity.Producto;

public class ProductoRepository {

    private final ProductoDao productoDao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public ProductoRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        productoDao = database.productoDao();
    }

    // Obtener todos los productos (Paginado)
    public LiveData<PagingData<Producto>> getAllProductosPagingData() {
        Pager<Integer, Producto> pager = new Pager<>(
                new PagingConfig(20, 10, false, 100),
                productoDao::getAllProductosPagingSource
        );
        return PagingLiveData.getLiveData(pager);
    }

    // Obtener producto por ID
    public LiveData<Producto> getProductoById(int idProducto) {
        return productoDao.getProductoById(idProducto);
    }

    // Obtener productos por categoría (Paginado)
    public LiveData<PagingData<Producto>> getProductosByCategoriaPagingData(String categoria) {
        Pager<Integer, Producto> pager = new Pager<>(
                new PagingConfig(20, 10, false, 100),
                () -> productoDao.getProductosByCategoriaPagingSource(categoria)
        );
        return PagingLiveData.getLiveData(pager);
    }

    // Obtener productos disponibles (Paginado)
    public LiveData<PagingData<Producto>> getProductosDisponiblesPagingData() {
        Pager<Integer, Producto> pager = new Pager<>(
                new PagingConfig(20, 10, false, 100),
                productoDao::getProductosDisponiblesPagingSource
        );
        return PagingLiveData.getLiveData(pager);
    }

    // Buscar productos por nombre (Paginado)
    public LiveData<PagingData<Producto>> buscarProductosPorNombrePagingData(String nombre) {
        Pager<Integer, Producto> pager = new Pager<>(
                new PagingConfig(20, 10, false, 100),
                () -> productoDao.buscarProductosPorNombrePagingSource(nombre)
        );
        return PagingLiveData.getLiveData(pager);
    }

    // Insertar producto
    public CompletableFuture<Long> insertProducto(Producto producto) {
        return CompletableFuture.supplyAsync(() -> {
            return productoDao.insertProducto(producto);
        }, executor);
    }

    // Actualizar producto
    public CompletableFuture<Integer> updateProducto(Producto producto) {
        return CompletableFuture.supplyAsync(() -> {
            return productoDao.updateProducto(producto);
        }, executor);
    }

    // Eliminar producto
    public CompletableFuture<Integer> deleteProducto(Producto producto) {
        return CompletableFuture.supplyAsync(() -> {
            return productoDao.deleteProducto(producto);
        }, executor);
    }

    // Eliminar producto por ID
    public CompletableFuture<Integer> deleteProductoById(int idProducto) {
        return CompletableFuture.supplyAsync(() -> {
            return productoDao.deleteProductoById(idProducto);
        }, executor);
    }

    // Actualizar disponibilidad del producto
    public CompletableFuture<Integer> updateDisponibilidadProducto(int idProducto, boolean disponible) {
        return CompletableFuture.supplyAsync(() -> {
            return productoDao.updateDisponibilidadProducto(idProducto, disponible);
        }, executor);
    }
}