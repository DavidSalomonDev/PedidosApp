package sv.edu.ues.pedidosapp.utils;

public class Constants {

    // Estados de pedidos
    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_CONFIRMADO = "CONFIRMADO";
    public static final String ESTADO_EN_PREPARACION = "EN_PREPARACION";
    public static final String ESTADO_ENTREGADO = "ENTREGADO";
    public static final String ESTADO_CANCELADO = "CANCELADO";

    // Categorías de productos
    public static final String CATEGORIA_COMIDA = "COMIDA";
    public static final String CATEGORIA_BEBIDA = "BEBIDA";
    public static final String CATEGORIA_POSTRE = "POSTRE";

    // Base de datos
    public static final String DATABASE_NAME = "pedidos_database";
    public static final int DATABASE_VERSION = 1;

    // Shared Preferences
    public static final String PREFS_NAME = "PedidosAppPrefs";
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_USER_EMAIL = "user_email";
    public static final String PREF_USER_NAME = "user_name";
    public static final String PREF_USER_PHONE = "user_phone";
    public static final String PREF_USER_ADDRESS = "user_address";
    public static final String PREF_IS_LOGGED_IN = "is_logged_in";
    public static final String PREF_THEME_MODE = "theme_mode";

    // Temas
    public static final String THEME_LIGHT = "light";
    public static final String THEME_DARK = "dark";
    public static final String THEME_SYSTEM = "system";

    // Valores por defecto
    public static final long DEFAULT_USER_ID = -1L;
    public static final String DEFAULT_THEME = THEME_SYSTEM;

    // Códigos de request
    public static final int REQUEST_IMAGE_PICK = 1001;
    public static final int REQUEST_CAMERA = 1002;
    public static final int REQUEST_PERMISSIONS = 1003;
}