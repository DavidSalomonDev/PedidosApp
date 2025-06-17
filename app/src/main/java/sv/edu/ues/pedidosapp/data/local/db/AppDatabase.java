package sv.edu.ues.pedidosapp.data.local.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sv.edu.ues.pedidosapp.data.local.dao.DetallePedidoDao;
import sv.edu.ues.pedidosapp.data.local.dao.PedidoDao;
import sv.edu.ues.pedidosapp.data.local.dao.ProductoDao;
import sv.edu.ues.pedidosapp.data.local.dao.UsuarioDao;
import sv.edu.ues.pedidosapp.data.local.entity.DetallePedido;
import sv.edu.ues.pedidosapp.data.local.entity.Pedido;
import sv.edu.ues.pedidosapp.data.local.entity.Producto;
import sv.edu.ues.pedidosapp.data.local.entity.Usuario;
import sv.edu.ues.pedidosapp.utils.Constants;

@Database(
        entities = {
                Usuario.class,
                Producto.class,
                Pedido.class,
                DetallePedido.class
        },
        version = Constants.DATABASE_VERSION,
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    // DAOs abstractos
    public abstract UsuarioDao usuarioDao();

    public abstract ProductoDao productoDao();

    public abstract PedidoDao pedidoDao();

    public abstract DetallePedidoDao detallePedidoDao();

    // Instancia singleton
    private static volatile AppDatabase INSTANCE;

    // Executor para operaciones de base de datos
    private static final int NUMBER_OF_THREADS = 4;
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
                                    Constants.DATABASE_NAME
                            )
                            .addCallback(roomDatabaseCallback)
                            .fallbackToDestructiveMigration() // Para desarrollo, eliminar en producción
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Callback para poblar la base de datos con datos iniciales
    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Poblar la base de datos con datos iniciales
            databaseWriteExecutor.execute(() -> {
                // Obtener DAOs
                UsuarioDao usuarioDao = INSTANCE.usuarioDao();
                ProductoDao productoDao = INSTANCE.productoDao();

                // Insertar usuario administrador por defecto
                Usuario adminUser = new Usuario(
                        "Administrador",
                        "admin@pedidosapp.com",
                        "admin123", // En producción, usar hash
                        "7777-7777",
                        "Dirección Admin",
                        System.currentTimeMillis()
                );
                usuarioDao.insertUsuario(adminUser);

                // Insertar productos de ejemplo
                insertSampleProducts(productoDao);
            });
        }
    };

    // Método para insertar productos de ejemplo
    private static void insertSampleProducts(ProductoDao productoDao) {
        // Comidas
        Producto[] comidas = {
                new Producto("Hamburguesa Clásica", "Hamburguesa con carne, lechuga, tomate y queso",
                        8.50, Constants.CATEGORIA_COMIDA, "https://example.com/hamburguesa.jpg", true),
                new Producto("Pizza Margherita", "Pizza con salsa de tomate, mozzarella y albahaca",
                        12.00, Constants.CATEGORIA_COMIDA, "https://example.com/pizza.jpg", true),
                new Producto("Pollo a la Plancha", "Pechuga de pollo con vegetales",
                        10.75, Constants.CATEGORIA_COMIDA, "https://example.com/pollo.jpg", true),
                new Producto("Ensalada César", "Lechuga, pollo, crutones y aderezo césar",
                        7.25, Constants.CATEGORIA_COMIDA, "https://example.com/ensalada.jpg", true),
                new Producto("Tacos de Carne", "3 tacos con carne asada, cebolla y cilantro",
                        9.00, Constants.CATEGORIA_COMIDA, "https://example.com/tacos.jpg", true)
        };

        // Bebidas
        Producto[] bebidas = {
                new Producto("Coca Cola", "Refresco de cola 355ml",
                        1.50, Constants.CATEGORIA_BEBIDA, "https://example.com/coca.jpg", true),
                new Producto("Agua Natural", "Botella de agua 500ml",
                        1.00, Constants.CATEGORIA_BEBIDA, "https://example.com/agua.jpg", true),
                new Producto("Jugo de Naranja", "Jugo natural de naranja 300ml",
                        2.25, Constants.CATEGORIA_BEBIDA, "https://example.com/jugo.jpg", true),
                new Producto("Café Americano", "Café negro recién preparado",
                        2.00, Constants.CATEGORIA_BEBIDA, "https://example.com/cafe.jpg", true),
                new Producto("Smoothie de Fresa", "Batido de fresa con yogurt",
                        3.50, Constants.CATEGORIA_BEBIDA, "https://example.com/smoothie.jpg", true)
        };

        // Postres
        Producto[] postres = {
                new Producto("Cheesecake", "Pastel de queso con fresas",
                        4.50, Constants.CATEGORIA_POSTRE, "https://example.com/cheesecake.jpg", true),
                new Producto("Brownie", "Brownie de chocolate con helado",
                        3.75, Constants.CATEGORIA_POSTRE, "https://example.com/brownie.jpg", true),
                new Producto("Flan", "Flan casero con caramelo",
                        3.00, Constants.CATEGORIA_POSTRE, "https://example.com/flan.jpg", true),
                new Producto("Helado", "Helado de vainilla, chocolate o fresa",
                        2.50, Constants.CATEGORIA_POSTRE, "https://example.com/helado.jpg", true)
        };

        // Insertar todos los productos
        for (Producto comida : comidas) {
            productoDao.insertProducto(comida);
        }
        for (Producto bebida : bebidas) {
            productoDao.insertProducto(bebida);
        }
        for (Producto postre : postres) {
            productoDao.insertProducto(postre);
        }
    }

    // Método para cerrar la base de datos (útil para testing)
    public static void closeDatabase() {
        if (INSTANCE != null) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }

    // Migración de ejemplo (para futuras versiones)
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Ejemplo: Agregar nueva columna
            // database.execSQL("ALTER TABLE usuarios ADD COLUMN fecha_nacimiento INTEGER");
        }
    };
}