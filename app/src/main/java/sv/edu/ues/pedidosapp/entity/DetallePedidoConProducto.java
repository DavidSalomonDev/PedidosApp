package sv.edu.ues.pedidosapp.entity;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

public class DetallePedidoConProducto {
    @Embedded
    public DetallePedido detalle;

    @ColumnInfo(name = "producto_nombre")
    public String productoNombre;

    @ColumnInfo(name = "imagen_url")
    public String imagenUrl;

    public DetallePedido getDetalle() {
        return detalle;
    }

    public void setDetalle(DetallePedido detalle) {
        this.detalle = detalle;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}