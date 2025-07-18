package sv.edu.ues.pedidosapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import sv.edu.ues.pedidosapp.data.local.AppDatabase;
import sv.edu.ues.pedidosapp.data.local.dao.UsuarioDao;
import sv.edu.ues.pedidosapp.data.local.entity.Usuario;

public class UsuarioRepository extends BaseRepository {

    private UsuarioDao usuarioDao;
    private LiveData<List<Usuario>> allUsuarios;

    public UsuarioRepository(Application application) {
        super(application);
        usuarioDao = database.usuarioDao();
        allUsuarios = usuarioDao.getAllUsuarios();
    }

    // Obtener todos los usuarios
    public LiveData<List<Usuario>> getAllUsuarios() {
        return allUsuarios;
    }

    // Obtener usuario por ID (LiveData)
    public LiveData<Usuario> getUsuarioById(int idUsuario) {
        return usuarioDao.getUsuarioById(idUsuario);
    }

    // Obtener usuario por ID (asíncrono) - NUEVO MÉTODO
    public CompletableFuture<Usuario> obtenerUsuarioPorId(long idUsuario) {
        return CompletableFuture.supplyAsync(() -> {
            return usuarioDao.getUsuarioByIdSync((int) idUsuario);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Insertar usuario
    public CompletableFuture<Long> insertUsuario(Usuario usuario) {
        return CompletableFuture.supplyAsync(() -> {
            return usuarioDao.insertUsuario(usuario);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Actualizar usuario
    public CompletableFuture<Integer> updateUsuario(Usuario usuario) {
        return CompletableFuture.supplyAsync(() -> {
            return usuarioDao.updateUsuario(usuario);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Eliminar usuario
    public CompletableFuture<Integer> deleteUsuario(Usuario usuario) {
        return CompletableFuture.supplyAsync(() -> {
            return usuarioDao.deleteUsuario(usuario);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Login - verificar credenciales
    public CompletableFuture<Usuario> login(String email, String password) {
        return CompletableFuture.supplyAsync(() -> {
            return usuarioDao.login(email, password);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Verificar si existe un email
    public CompletableFuture<Boolean> checkEmailExists(String email) {
        return CompletableFuture.supplyAsync(() -> {
            return usuarioDao.checkEmailExists(email) > 0;
        }, AppDatabase.databaseWriteExecutor);
    }

    // Obtener usuario por email (asíncrono) - NUEVO MÉTODO
    public CompletableFuture<Usuario> obtenerUsuarioPorEmail(String email) {
        return CompletableFuture.supplyAsync(() -> {
            return usuarioDao.getUsuarioByEmail(email);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Registrar nuevo usuario
    public CompletableFuture<RegistroResult> registrarUsuario(Usuario usuario) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Verificar si el email ya existe
                if (usuarioDao.checkEmailExists(usuario.getEmail()) > 0) {
                    return new RegistroResult(false, "El email ya está registrado", -1);
                }

                // Insertar usuario
                long userId = usuarioDao.insertUsuario(usuario);
                if (userId > 0) {
                    return new RegistroResult(true, "Usuario registrado exitosamente", userId);
                } else {
                    return new RegistroResult(false, "Error al registrar usuario", -1);
                }
            } catch (Exception e) {
                return new RegistroResult(false, "Error: " + e.getMessage(), -1);
            }
        }, AppDatabase.databaseWriteExecutor);
    }

    // Obtener usuario por email (síncrono) - mantenemos el método original
    public CompletableFuture<Usuario> getUsuarioByEmail(String email) {
        return CompletableFuture.supplyAsync(() -> {
            return usuarioDao.getUsuarioByEmail(email);
        }, AppDatabase.databaseWriteExecutor);
    }

    // Clase para resultado de registro
    public static class RegistroResult {
        private boolean success;
        private String message;
        private long userId;

        public RegistroResult(boolean success, String message, long userId) {
            this.success = success;
            this.message = message;
            this.userId = userId;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public long getUserId() {
            return userId;
        }
    }
}