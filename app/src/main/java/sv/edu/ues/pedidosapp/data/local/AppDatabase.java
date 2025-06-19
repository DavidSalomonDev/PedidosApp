package sv.edu.ues.pedidosapp.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sv.edu.ues.pedidosapp.data.local.dao.DetallePedidoDao;
import sv.edu.ues.pedidosapp.data.local.dao.PedidoDao;
import sv.edu.ues.pedidosapp.data.local.dao.ProductoDao;
import sv.edu.ues.pedidosapp.data.local.dao.UsuarioDao;
import sv.edu.ues.pedidosapp.data.local.db.Converters;
import sv.edu.ues.pedidosapp.data.local.entity.DetallePedido;
import sv.edu.ues.pedidosapp.data.local.entity.Pedido;
import sv.edu.ues.pedidosapp.data.local.entity.Producto;
import sv.edu.ues.pedidosapp.data.local.entity.Usuario;

@Database(entities = {Usuario.class, Producto.class, Pedido.class, DetallePedido.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static AppDatabase instance;

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Poblar la base de datos con datos semilla usando SeedDataHelper
            SeedDataHelper.insertSeedData(instance);
        }
    };

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "pedidos_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(sRoomDatabaseCallback)
                    .build();
        }
        return instance;
    }

    // Métodos abstractos para obtener los DAOs
    public abstract UsuarioDao usuarioDao();
    public abstract ProductoDao productoDao();
    public abstract PedidoDao pedidoDao();
    public abstract DetallePedidoDao detallePedidoDao();
}