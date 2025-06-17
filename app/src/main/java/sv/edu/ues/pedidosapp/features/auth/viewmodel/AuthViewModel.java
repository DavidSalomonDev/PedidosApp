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

    public AuthViewModel(@NonNull Application application) {
        super(application);
        usuarioRepository = repositoryManager.getUsuarioRepository();
    }

    // LiveData para observar el resultado de la autenticación
    public LiveData<AuthResult> getAuthResult() {
        return authResult;
    }

    // Método para registrar un nuevo usuario
    public void registrarUsuario(Usuario usuario) {
        usuarioRepository.registrarUsuario(usuario)
                .thenAccept(result -> {
                    authResult.postValue(new AuthResult(result.isSuccess(), result.getMessage(), result.getUserId()));
                })
                .exceptionally(e -> {
                    authResult.postValue(new AuthResult(false, "Error: " + e.getMessage(), -1));
                    return null;
                });
    }

    // Método para iniciar sesión
    public void login(String email, String password) {
        usuarioRepository.login(email, password)
                .thenAccept(usuario -> {
                    if (usuario != null) {
                        authResult.postValue(new AuthResult(true, "Inicio de sesión exitoso", usuario.getIdUsuario()));
                    } else {
                        authResult.postValue(new AuthResult(false, "Credenciales incorrectas", -1));
                    }
                })
                .exceptionally(e -> {
                    authResult.postValue(new AuthResult(false, "Error: " + e.getMessage(), -1));
                    return null;
                });
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
    }
}