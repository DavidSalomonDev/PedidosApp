package sv.edu.ues.pedidosapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                Usuario.class,
                Producto.class,
                Mesa.class,
                Pedido.class,
                DetallePedido.class
        },
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UsuarioDao usuarioDao();

    public abstract ProductoDao productoDao();

    public abstract MesaDao mesaDao();

    public abstract PedidoDao pedidoDao();

    public abstract DetallePedidoDao detallePedidoDao();
}