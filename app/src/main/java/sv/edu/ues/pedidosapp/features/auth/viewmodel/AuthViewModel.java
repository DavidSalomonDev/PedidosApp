package sv.edu.ues.pedidosapp.features.auth.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import sv.edu.ues.pedidosapp.data.local.entity.Usuario;
import sv.edu.ues.pedidosapp.data.repository.UsuarioRepository;
import sv.edu.ues.pedidosapp.features.core.BaseViewModel;

public class AuthViewModel extends BaseViewModel {

    private final UsuarioRepository usuarioRepository;
    private final MutableLiveData<AuthResult> authResult = new MutableLiveData<>();
    private final MutableLiveData<Usuario> usuarioCompleto = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        usuarioRepository = repositoryManager.getUsuarioRepository();
    }

    // LiveData para observar el resultado de la autenticación
    public LiveData<AuthResult> getAuthResult() {
        return authResult;
    }

    // LiveData para observar el usuario completo
    public LiveData<Usuario> getUsuarioCompleto() {
        return usuarioCompleto;
    }

    // Método para registrar un nuevo usuario
    public void registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            authResult.postValue(new AuthResult(false, "Datos de usuario inválidos", -1));
            return;
        }

        usuarioRepository.registrarUsuario(usuario)
                .thenAccept(result -> {
                    if (result != null) {
                        authResult.postValue(new AuthResult(result.isSuccess(), result.getMessage(), result.getUserId()));
                    } else {
                        authResult.postValue(new AuthResult(false, "Error desconocido al registrar usuario", -1));
                    }
                })
                .exceptionally(e -> {
                    String errorMessage = e != null && e.getMessage() != null ? e.getMessage() : "Error desconocido";
                    authResult.postValue(new AuthResult(false, "Error: " + errorMessage, -1));
                    return null;
                });
    }

    // Método para iniciar sesión
    public void login(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            authResult.postValue(new AuthResult(false, "Email y contraseña son requeridos", -1));
            return;
        }

        usuarioRepository.login(email.trim(), password)
                .thenAccept(usuario -> {
                    if (usuario != null) {
                        authResult.postValue(new AuthResult(true, "Inicio de sesión exitoso", usuario.getIdUsuario()));
                        usuarioCompleto.postValue(usuario);
                    } else {
                        authResult.postValue(new AuthResult(false, "Credenciales incorrectas", -1));
                    }
                })
                .exceptionally(e -> {
                    String errorMessage = e != null && e.getMessage() != null ? e.getMessage() : "Error desconocido";
                    authResult.postValue(new AuthResult(false, "Error: " + errorMessage, -1));
                    return null;
                });
    }

    // Método para obtener usuario por ID (usando el método que agregamos al repository)
    public void obtenerUsuarioPorId(long userId) {
        if (userId <= 0) {
            usuarioCompleto.postValue(null);
            return;
        }

        usuarioRepository.obtenerUsuarioPorId(userId)
                .thenAccept(usuarioCompleto::postValue)
                .exceptionally(e -> {
                    usuarioCompleto.postValue(null);
                    return null;
                });
    }

    // Método para validar si un email ya existe
    public void validarEmailExistente(String email) {
        if (email == null || email.trim().isEmpty()) {
            return;
        }

        usuarioRepository.obtenerUsuarioPorEmail(email.trim())
                .thenAccept(usuario -> {
                    if (usuario != null) {
                        authResult.postValue(new AuthResult(false, "El email ya está registrado", -1));
                    }
                })
                .exceptionally(e -> {
                    // Si hay error al buscar, asumimos que no existe y continuamos
                    return null;
                });
    }

    // Método para limpiar los resultados
    public void clearAuthResult() {
        authResult.setValue(null);
    }

    public void clearUsuarioCompleto() {
        usuarioCompleto.setValue(null);
    }

    // Clase para encapsular el resultado de la autenticación
    public static class AuthResult {
        private final boolean success;
        private final String message;
        private final long userId;

        public AuthResult(boolean success, String message, long userId) {
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

        @NonNull
        @Override
        public String toString() {
            return "AuthResult{" +
                    "success=" + success +
                    ", message='" + message + '\'' +
                    ", userId=" + userId +
                    '}';
        }
    }
}