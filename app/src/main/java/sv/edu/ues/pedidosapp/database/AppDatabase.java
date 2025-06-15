package sv.edu.ues.pedidosapp.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sv.edu.ues.pedidosapp.dao.PedidoDao;
import sv.edu.ues.pedidosapp.dao.DetallePedidoDao;
import sv.edu.ues.pedidosapp.dao.ProductoDao;
import sv.edu.ues.pedidosapp.dao.UsuarioDao;
import sv.edu.ues.pedidosapp.entity.Pedido;
import sv.edu.ues.pedidosapp.entity.DetallePedido;
import sv.edu.ues.pedidosapp.entity.Producto;
import sv.edu.ues.pedidosapp.entity.Usuario;

@Database(
        entities = {
                Usuario.class,
                Producto.class,
                Pedido.class,
                DetallePedido.class
        },
        version = 1,
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    // DAOs abstractos
    public abstract UsuarioDao usuarioDao();
    public abstract ProductoDao productoDao();
    public abstract PedidoDao pedidoDao();
    public abstract DetallePedidoDao detallePedidoDao();

    // Instancia singleton de la base de datos
    private static volatile AppDatabase INSTANCE;

    // Número de hilos para operaciones de base de datos
    private static final int NUMBER_OF_THREADS = 4;

    // Executor para operaciones de escritura en background
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Método para obtener la instancia de la base de datos
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "pedidos_database"
                            )
                            .fallbackToDestructiveMigration() // Para desarrollo, elimina datos en cambios de esquema
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Método para cerrar la base de datos (opcional, para testing)
    public static void closeDatabase() {
        if (INSTANCE != null) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
}