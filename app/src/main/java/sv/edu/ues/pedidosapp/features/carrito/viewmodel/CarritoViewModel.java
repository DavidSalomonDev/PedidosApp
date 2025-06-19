// sv/edu/ues/pedidosapp/features/carrito/viewmodel/CarritoViewModel.java
package sv.edu.ues.pedidosapp.features.carrito.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.pedidosapp.data.local.entity.Producto;
import sv.edu.ues.pedidosapp.data.model.ProductoSeleccionado;

public class CarritoViewModel extends AndroidViewModel {

    private MutableLiveData<List<ProductoSeleccionado>> productosSeleccionados = new MutableLiveData<>();
    private MutableLiveData<Double> totalCarrito = new MutableLiveData<>();
    private List<ProductoSeleccionado> carrito = new ArrayList<>();

    public CarritoViewModel(@NonNull Application application) {
        super(application);
        productosSeleccionados.setValue(carrito);
        totalCarrito.setValue(0.0);
    }

    // Agregar producto al carrito
    public void agregarProducto(Producto producto, int cantidad) {
        // Verificar si el producto ya está en el carrito
        for (ProductoSeleccionado item : carrito) {
            if (item.getProducto().getIdProducto() == producto.getIdProducto()) {
                // Si ya existe, actualizar la cantidad
                item.setCantidad(item.getCantidad() + cantidad);
                actualizarCarrito();
                return;
            }
        }

        // Si no existe, agregar nuevo producto
        ProductoSeleccionado nuevoItem = new ProductoSeleccionado(producto, cantidad);
        carrito.add(nuevoItem);
        actualizarCarrito();
    }

    // Actualizar cantidad de un producto en el carrito
    public void actualizarCantidad(int idProducto, int nuevaCantidad) {
        for (ProductoSeleccionado item : carrito) {
            if (item.getProducto().getIdProducto() == idProducto) {
                if (nuevaCantidad <= 0) {
                    carrito.remove(item);
                } else {
                    item.setCantidad(nuevaCantidad);
                }
                actualizarCarrito();
                return;
            }
        }
    }

    // Eliminar producto del carrito
    public void eliminarProducto(int idProducto) {
        carrito.removeIf(item -> item.getProducto().getIdProducto() == idProducto);
        actualizarCarrito();
    }

    // Limpiar carrito
    public void limpiarCarrito() {
        carrito.clear();
        actualizarCarrito();
    }

    // Actualizar el carrito y calcular el total
    private void actualizarCarrito() {
        productosSeleccionados.setValue(new ArrayList<>(carrito));
        calcularTotal();
    }

    // Calcular el total del carrito
    private void calcularTotal() {
        double total = 0.0;
        for (ProductoSeleccionado item : carrito) {
            total += item.getSubtotal();
        }
        totalCarrito.setValue(total);
    }

    // Obtener productos seleccionados
    public LiveData<List<ProductoSeleccionado>> getProductosSeleccionados() {
        return productosSeleccionados;
    }

    // Obtener total del carrito
    public LiveData<Double> getTotalCarrito() {
        return totalCarrito;
    }

    // Obtener cantidad de productos en el carrito
    public int getCantidadProductos() {
        return carrito.size();
    }
}