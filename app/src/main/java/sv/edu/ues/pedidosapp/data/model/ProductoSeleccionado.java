// sv/edu/ues/pedidosapp/data/model/ProductoSeleccionado.java
package sv.edu.ues.pedidosapp.data.model;

import sv.edu.ues.pedidosapp.data.local.entity.Producto;

public class ProductoSeleccionado {

    private Producto producto;
    private int cantidad;
    private double subtotal;

    public ProductoSeleccionado(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = producto.getPrecio() * cantidad;
    }

    // Getters y Setters
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        this.subtotal = producto.getPrecio() * cantidad; // Recalcular subtotal
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = producto.getPrecio() * cantidad; // Recalcular subtotal
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}