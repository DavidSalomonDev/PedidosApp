package sv.edu.ues.pedidosapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import sv.edu.ues.pedidosapp.data.local.entity.Usuario;

public class SessionManager {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Métodos para gestión de sesión
    public void createLoginSession(Usuario usuario) {
        editor.putBoolean(Constants.PREF_IS_LOGGED_IN, true);
        editor.putLong(Constants.PREF_USER_ID, usuario.getIdUsuario());
        editor.putString(Constants.PREF_USER_EMAIL, usuario.getEmail());
        editor.putString(Constants.PREF_USER_NAME, usuario.getNombre());
        editor.putString(Constants.PREF_USER_PHONE, usuario.getTelefono());
        editor.putString(Constants.PREF_USER_ADDRESS, usuario.getDireccion());
        editor.apply();
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(Constants.PREF_IS_LOGGED_IN, false);
    }

    public long getUserId() {
        return sharedPreferences.getLong(Constants.PREF_USER_ID, Constants.DEFAULT_USER_ID);
    }

    public String getUserEmail() {
        return sharedPreferences.getString(Constants.PREF_USER_EMAIL, "");
    }

    public String getUserName() {
        return sharedPreferences.getString(Constants.PREF_USER_NAME, "");
    }

    public String getUserPhone() {
        return sharedPreferences.getString(Constants.PREF_USER_PHONE, "");
    }

    public String getUserAddress() {
        return sharedPreferences.getString(Constants.PREF_USER_ADDRESS, "");
    }

    public String getThemeMode() {
        return sharedPreferences.getString(Constants.PREF_THEME_MODE, Constants.DEFAULT_THEME);
    }

    // Métodos para gestión de tema
    public void setThemeMode(String themeMode) {
        editor.putString(Constants.PREF_THEME_MODE, themeMode);
        editor.commit();
    }

    // Método para obtener todos los datos del usuario actual
    public Usuario getCurrentUser() {
        if (!isLoggedIn()) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(getUserId());
        usuario.setNombre(getUserName());
        usuario.setEmail(getUserEmail());
        usuario.setTelefono(getUserPhone());
        usuario.setDireccion(getUserAddress());
        // No guardamos ni recuperamos la contraseña ni la fecha de registro en sesión
        return usuario;
    }

    // Método para actualizar datos del usuario en sesión (útil si el usuario edita su perfil)
    public void updateUserSession(Usuario usuario) {
        if (usuario != null && isLoggedIn()) {
            editor.putString(Constants.PREF_USER_NAME, usuario.getNombre());
            editor.putString(Constants.PREF_USER_EMAIL, usuario.getEmail());
            editor.putString(Constants.PREF_USER_PHONE, usuario.getTelefono());
            editor.putString(Constants.PREF_USER_ADDRESS, usuario.getDireccion());
            editor.apply();
        }
    }

    // Método para verificar si los datos de sesión están completos
    public boolean isSessionDataComplete() {
        return isLoggedIn() &&
                getUserId() != Constants.DEFAULT_USER_ID &&
                !getUserEmail().isEmpty() &&
                !getUserName().isEmpty();
    }

    // Método para obtener información resumida del usuario (para mostrar en UI)
    public String getUserDisplayInfo() {
        if (!isLoggedIn()) {
            return "Usuario no logueado";
        }

        String name = getUserName();
        String email = getUserEmail();

        if (!name.isEmpty()) {
            return name;
        } else if (!email.isEmpty()) {
            return email;
        } else {
            return "Usuario ID: " + getUserId();
        }
    }

    // Método para debug (solo para desarrollo)
    public void printSessionData() {
        if (isLoggedIn()) {
            System.out.println("=== DATOS DE SESIÓN ===");
            System.out.println("ID: " + getUserId());
            System.out.println("Nombre: " + getUserName());
            System.out.println("Email: " + getUserEmail());
            System.out.println("Teléfono: " + getUserPhone());
            System.out.println("Dirección: " + getUserAddress());
            System.out.println("Tema: " + getThemeMode());
            System.out.println("=====================");
        } else {
            System.out.println("No hay sesión activa");
        }
    }
}