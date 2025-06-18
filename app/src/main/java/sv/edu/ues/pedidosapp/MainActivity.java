package sv.edu.ues.pedidosapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sv.edu.ues.pedidosapp.data.local.db.AppDatabase;
import sv.edu.ues.pedidosapp.data.local.entity.Usuario;

public class MainActivity extends AppCompatActivity {

    private EditText txtUsuario, txtPassword;
    private Button btnIngresar, btnRegistrarse, btnSalir;
    private AppDatabase database;
    private ExecutorService executor;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarComponentes();
        configurarEventos();

        // Inicializar base de datos, executor y SharedPreferences
        database = AppDatabase.getDatabase(this);
        executor = Executors.newSingleThreadExecutor();
        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);

        // Verificar si hay sesión guardada
        verificarSesionGuardada();
    }

    private void inicializarComponentes() {
        txtUsuario = findViewById(R.id.txtUsuario);
        txtPassword = findViewById(R.id.txtPassword);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnSalir = findViewById(R.id.btnSalir);
    }

    private void configurarEventos() {
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity(); // Cierra la aplicación completamente
            }
        });
    }

    private void verificarSesionGuardada() {
        boolean sesionActiva = sharedPreferences.getBoolean("sesion_activa", false);
        if (sesionActiva) {
            String emailGuardado = sharedPreferences.getString("usuario_email", "");
            String nombreGuardado = sharedPreferences.getString("usuario_nombre", "");
            if (!emailGuardado.isEmpty()) {
                mostrarMensaje("Bienvenido de nuevo " + nombreGuardado);
                irAPantallaPrincipal();
            }
        }
    }

    private void iniciarSesion() {
        String usuario = txtUsuario.getText().toString().trim();
        String password = txtPassword.getText().toString();

        // Validar campos
        if (!validarCampos(usuario, password)) {
            return;
        }

        // Deshabilitar botón mientras se procesa
        btnIngresar.setEnabled(false);
        btnIngresar.setText("Verificando...");

        // Autenticar usuario usando ExecutorService
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Usuario usuarioEncontrado = null;

                    // Buscar por email o por nombre
                    if (Patterns.EMAIL_ADDRESS.matcher(usuario).matches()) {
                        usuarioEncontrado = database.usuarioDao().getUsuarioByName(usuario);
                    } else {
                        // Buscar por nombre y luego verificar password
                        Usuario usuarioTemp = database.usuarioDao().getUsuarioByEmail(usuario);
                        if (usuarioTemp != null && usuarioTemp.getPassword().equals(password)) {
                            usuarioEncontrado = usuarioTemp;
                        }
                    }

                    final Usuario finalUsuario = usuarioEncontrado;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Actualizar UI aquí
                            if (finalUsuario != null) {
                                // Login exitoso
                                guardarSesion(finalUsuario);
                                mostrarMensaje("Bienvenido " + finalUsuario.getNombre());
                                irAPantallaPrincipal();
                            } else {
                                // Login fallido
                                mostrarMensaje("Usuario o contraseña incorrectos");
                                txtPassword.setText("");
                                txtUsuario.requestFocus();
                            }
                            btnIngresar.setEnabled(true);
                            btnIngresar.setText("INGRESAR");
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mostrarMensaje("Error al conectar con la base de datos");
                            btnIngresar.setEnabled(true);
                            btnIngresar.setText("INGRESAR");
                        }
                    });
                }
            }

        });
    }

    private boolean validarCampos(String usuario, String password) {
        // Validar usuario (puede ser email o nombre)
        if (usuario.isEmpty()) {
            txtUsuario.setError("Ingrese su usuario o email");
            txtUsuario.requestFocus();
            return false;
        }

        if (usuario.length() < 3) {
            txtUsuario.setError("El usuario debe tener al menos 3 caracteres");
            txtUsuario.requestFocus();
            return false;
        }

        // Validar contraseña
        if (password.isEmpty()) {
            txtPassword.setError("Ingrese su contraseña");
            txtPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            txtPassword.setError("La contraseña debe tener al menos 6 caracteres");
            txtPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void guardarSesion(Usuario usuario) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario_email", usuario.getEmail());
        editor.putString("usuario_nombre", usuario.getNombre());
        editor.putString("usuario_telefono", usuario.getTelefono());
        editor.putString("usuario_direccion", usuario.getDireccion());
        editor.putInt("usuario_id", usuario.getIdUsuario());
        editor.putBoolean("sesion_activa", true);
        editor.putLong("timestamp_login", System.currentTimeMillis());
        editor.apply();

        // Log para debug
        System.out.println("Sesión guardada para: " + usuario.getNombre());
    }

    private void irAPantallaPrincipal() {
        // Aquí debes cambiar por tu actividad principal de la app
        // Por ejemplo, si tienes una HomeActivity o MenuActivity:
        // Intent intent = new Intent(this, HomeActivity.class);
        // startActivity(intent);
        // finish();

        // Por ahora, crear una actividad temporal para mostrar que funciona
        Intent intent = new Intent(this, DashboardActivity_test.class);
        startActivity(intent);
        finish();
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Limpiar campos cuando regrese a esta actividad
        txtUsuario.setText("");
        txtPassword.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }

    // Método para cerrar sesión (usar en otras actividades)
    public static void cerrarSesion(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}