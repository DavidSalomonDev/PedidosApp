package sv.edu.ues.pedidosapp.features.productos.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagingData;

import sv.edu.ues.pedidosapp.data.local.entity.Producto;
import sv.edu.ues.pedidosapp.data.repository.ProductoRepository;

public class ProductoViewModel extends AndroidViewModel {

    private final ProductoRepository productoRepository;
    private final LiveData<PagingData<Producto>> productoPagingData;
    private final MutableLiveData<String> operationResult = new MutableLiveData<>();

    public ProductoViewModel(@NonNull Application application) {
        super(application);
        productoRepository = new ProductoRepository(application);
        productoPagingData = productoRepository.getAllProductosPagingData();
    }

    // Obtener todos los productos (Paginado)
    public LiveData<PagingData<Producto>> getAllProductosPagingData() {
        return productoPagingData;
    }

    // Obtener producto por ID
    public LiveData<Producto> getProductoById(int idProducto) {
        return productoRepository.getProductoById(idProducto);
    }

    // Obtener productos por categoría (Paginado)
    public LiveData<PagingData<Producto>> getProductosByCategoriaPagingData(String categoria) {
        return productoRepository.getProductosByCategoriaPagingData(categoria);
    }

    // Obtener productos disponibles (Paginado)
    public LiveData<PagingData<Producto>> getProductosDisponiblesPagingData() {
        return productoRepository.getProductosDisponiblesPagingData();
    }

    // Buscar productos por nombre (Paginado)
    public LiveData<PagingData<Producto>> buscarProductosPorNombrePagingData(String nombre) {
        return productoRepository.buscarProductosPorNombrePagingData(nombre);
    }

    // Insertar producto
    public void insertProducto(Producto producto) {
        productoRepository.insertProducto(producto).thenAccept(result -> {
            if (result > 0) {
                operationResult.postValue("Producto creado exitosamente");
            } else {
                operationResult.postValue("Error al crear el producto");
            }
        }).exceptionally(throwable -> {
            operationResult.postValue("Error: " + throwable.getMessage());
            return null;
        });
    }

    // Actualizar producto
    public void updateProducto(Producto producto) {
        productoRepository.updateProducto(producto).thenAccept(result -> {
            if (result > 0) {
                operationResult.postValue("Producto actualizado exitosamente");
            } else {
                operationResult.postValue("Error al actualizar el producto");
            }
        }).exceptionally(throwable -> {
            operationResult.postValue("Error: " + throwable.getMessage());
            return null;
        });
    }

    // Eliminar producto
    public void deleteProducto(Producto producto) {
        productoRepository.deleteProducto(producto).thenAccept(result -> {
            if (result > 0) {
                operationResult.postValue("Producto eliminado exitosamente");
            } else {
                operationResult.postValue("Error al eliminar el producto");
            }
        }).exceptionally(throwable -> {
            operationResult.postValue("Error: " + throwable.getMessage());
            return null;
        });
    }

    // Eliminar producto por ID
    public void deleteProductoById(int idProducto) {
        productoRepository.deleteProductoById(idProducto).thenAccept(result -> {
            if (result > 0) {
                operationResult.postValue("Producto eliminado exitosamente");
            } else {
                operationResult.postValue("Error al eliminar el producto");
            }
        }).exceptionally(throwable -> {
            operationResult.postValue("Error: " + throwable.getMessage());
            return null;
        });
    }

    // Actualizar disponibilidad del producto
    public void updateDisponibilidadProducto(int idProducto, boolean disponible) {
        productoRepository.updateDisponibilidadProducto(idProducto, disponible).thenAccept(result -> {
            if (result > 0) {
                operationResult.postValue("Disponibilidad del producto actualizada exitosamente");
            } else {
                operationResult.postValue("Error al actualizar la disponibilidad del producto");
            }
        }).exceptionally(throwable -> {
            operationResult.postValue("Error: " + throwable.getMessage());
            return null;
        });
    }

    // Obtener resultado de operaciones
    public LiveData<String> getOperationResult() {
        return operationResult;
    }
}