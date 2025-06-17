package sv.edu.ues.pedidosapp.data.local.db;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import sv.edu.ues.pedidosapp.data.local.entity.Producto;
import sv.edu.ues.pedidosapp.data.local.entity.Usuario;

public class DatabaseHelper {

    private final AppDatabase database;

    public DatabaseHelper(Context context) {
        database = AppDatabase.getDatabase(context);
    }

    // Método para verificar si la base de datos tiene datos
    public CompletableFuture<Boolean> isDatabaseEmpty() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Usuario> usuarios = database.usuarioDao().getAllUsuarios().getValue();
                List<Producto> productos = database.productoDao().getAllProductos().getValue();

                return (usuarios == null || usuarios.isEmpty()) &&
                        (productos == null || productos.isEmpty());
            } catch (Exception e) {
                return true; // Asumir vacía si hay error
            }
        }, AppDatabase.databaseWriteExecutor);
    }

    // Método para limpiar toda la base de datos
    public void clearAllTables() {
        AppDatabase.databaseWriteExecutor.execute(database::clearAllTables);
    }

    // Método para hacer backup de datos críticos
    public CompletableFuture<Boolean> backupCriticalData() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Implementar lógica de backup si es necesario
                // Por ejemplo, exportar a JSON o enviar a servidor
                return true;
            } catch (Exception e) {
                return false;
            }
        }, AppDatabase.databaseWriteExecutor);
    }

    // Método para obtener estadísticas de la base de datos
    public CompletableFuture<DatabaseStats> getDatabaseStats() {
        return CompletableFuture.supplyAsync(() -> {
            DatabaseStats stats = new DatabaseStats();

            try {
                // Contar registros en cada tabla
                stats.totalUsuarios = countUsuarios();
                stats.totalProductos = countProductos();
                stats.totalPedidos = countPedidos();
                stats.totalDetalles = countDetallesPedidos();

            } catch (Exception e) {
                // Manejar errores
            }

            return stats;
        }, AppDatabase.databaseWriteExecutor);
    }

    private int countUsuarios() {
        // Implementar conteo
        return 0;
    }

    private int countProductos() {
        // Implementar conteo
        return 0;
    }

    private int countPedidos() {
        // Implementar conteo
        return 0;
    }

    private int countDetallesPedidos() {
        // Implementar conteo
        return 0;
    }

    // Clase para estadísticas
    public static class DatabaseStats {
        public int totalUsuarios;
        public int totalProductos;
        public int totalPedidos;
        public int totalDetalles;

        @NonNull
        @Override
        public String toString() {
            return "DatabaseStats{" +
                    "usuarios=" + totalUsuarios +
                    ", productos=" + totalProductos +
                    ", pedidos=" + totalPedidos +
                    ", detalles=" + totalDetalles +
                    '}';
        }
    }
}