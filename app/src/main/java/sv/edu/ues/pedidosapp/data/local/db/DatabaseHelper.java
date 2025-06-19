package sv.edu.ues.pedidosapp.data.local.db;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import sv.edu.ues.pedidosapp.data.local.AppDatabase;
import sv.edu.ues.pedidosapp.data.local.entity.Producto;
import sv.edu.ues.pedidosapp.data.local.entity.Usuario;

public class DatabaseHelper {

    private final AppDatabase database;

    public DatabaseHelper(Context context) {
        database = AppDatabase.getInstance(context);
    }

    // Método para verificar si la base de datos tiene datos
    public CompletableFuture<Boolean> isDatabaseEmpty() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Usar métodos síncronos en lugar de LiveData
                int usuarioCount = database.usuarioDao().getUserCount();
                int productoCount = database.productoDao().getProductoCount();

                return usuarioCount == 0 && productoCount == 0;
            } catch (Exception e) {
                return true; // Asumir vacía si hay error
            }
        }, AppDatabase.databaseWriteExecutor);
    }

    // Método para limpiar toda la base de datos
    public void clearAllTables() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                // Limpiar cada tabla individualmente
                database.detallePedidoDao().deleteAllDetallesPedidos();
                database.pedidoDao().deleteAllPedidos();
                database.productoDao().deleteAllProductos();
                database.usuarioDao().deleteAllUsuarios();
            } catch (Exception e) {
                // Manejar error
            }
        });
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
        return database.usuarioDao().getUserCount();
    }

    private int countProductos() {
        return database.productoDao().getProductoCount();
    }

    private int countPedidos() {
        return database.pedidoDao().getPedidoCount();
    }

    private int countDetallesPedidos() {
        return database.detallePedidoDao().getDetallePedidoCount();
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