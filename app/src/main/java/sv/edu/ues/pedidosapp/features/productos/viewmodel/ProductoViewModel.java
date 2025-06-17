package sv.edu.ues.pedidosapp.features.productos.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import sv.edu.ues.pedidosapp.data.local.entity.Producto;
import sv.edu.ues.pedidosapp.data.repository.ProductoRepository;
import sv.edu.ues.pedidosapp.features.core.BaseViewModel;

public class ProductoViewModel extends BaseViewModel {

    private ProductoRepository productoRepository;
    private LiveData<List<Producto>> allProductos;
    private LiveData<List<Producto>> productosDisponibles;
    private MutableLiveData<ProductoResult> productoResult = new MutableLiveData<>();

    public ProductoViewModel(@NonNull Application application) {
        super(application);
        productoRepository = repositoryManager.getProductoRepository();
        allProductos = productoRepository.getAllProductos();
        productosDisponibles = productoRepository.getProductosDisponibles();
    }

    // LiveData para observar todos los productos
    public LiveData<List<Producto>> getAllProductos() {
        return allProductos;
    }

    // LiveData para observar productos disponibles
    public LiveData<List<Producto>> getProductosDisponibles() {
        return productosDisponibles;
    }

    // LiveData para observar el resultado de las operaciones
    public LiveData<ProductoResult> getProductoResult() {
        return productoResult;
    }

    // Obtener producto por ID
    public LiveData<Producto> getProductoById(int idProducto) {
        return productoRepository.getProductoById(idProducto);
    }

    // Obtener productos por categoría
    public LiveData<List<Producto>> getProductosByCategoria(String categoria) {
        return productoRepository.getProductosByCategoria(categoria);
    }

    // Buscar productos por nombre
    public LiveData<List<Producto>> searchProductosByNombre(String nombre) {
        return productoRepository.searchProductosByNombre(nombre);
    }

    // Obtener categorías
    public LiveData<List<String>> getCategorias() {
        return productoRepository.getCategorias();
    }

    // Insertar producto
    public void insertProducto(Producto producto) {
        productoRepository.insertProducto(producto)
                .thenAccept(id -> {
                    productoResult.postValue(new ProductoResult(true, "Producto creado exitosamente", id));
                })
                .exceptionally(e -> {
                    productoResult.postValue(new ProductoResult(false, "Error: " + e.getMessage(), -1));
                    return null;
                });
    }

    // Actualizar producto
    public void updateProducto(Producto producto) {
        productoRepository.updateProducto(producto)
                .thenAccept(result -> {
                    productoResult.postValue(new ProductoResult(result > 0, "Producto actualizado exitosamente", producto.getIdProducto()));
                })
                .exceptionally(e -> {
                    productoResult.postValue(new ProductoResult(false, "Error: " + e.getMessage(), -1));
                    return null;
                });
    }

    // Eliminar producto
    public void deleteProducto(Producto producto) {
        productoRepository.deleteProducto(producto)
                .thenAccept(result -> {
                    productoResult.postValue(new ProductoResult(result > 0, "Producto eliminado exitosamente", producto.getIdProducto()));
                })
                .exceptionally(e -> {
                    productoResult.postValue(new ProductoResult(false, "Error: " + e.getMessage(), -1));
                    return null;
                });
    }

    // Cambiar disponibilidad
    public void updateDisponibilidad(int idProducto, boolean disponible) {
        productoRepository.updateDisponibilidad(idProducto, disponible)
                .thenAccept(result -> {
                    productoResult.postValue(new ProductoResult(result > 0, "Disponibilidad actualizada", idProducto));
                })
                .exceptionally(e -> {
                    productoResult.postValue(new ProductoResult(false, "Error: " + e.getMessage(), -1));
                    return null;
                });
    }

    // Clase para encapsular el resultado de las operaciones
    public static class ProductoResult {
        private boolean success;
        private String message;
        private long productoId;

        public ProductoResult(boolean success, String message, long productoId) {
            this.success = success;
            this.message = message;
            this.productoId = productoId;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public long getProductoId() { return productoId; }
    }
}