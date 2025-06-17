package sv.edu.ues.pedidosapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sv.edu.ues.pedidosapp.data.local.entity.Usuario;
import sv.edu.ues.pedidosapp.data.local.db.AppDatabase;


public class RegistroActivity extends AppCompatActivity {

    private EditText txtUser, txtEmail, txtTelefono, txtContrasenia, txtContraseniaConfirm;
    private Button btnRegistrar, btnRegresar;
    private AppDatabase database;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        inicializarComponentes();
        configurarEventos();

        // Inicializar base de datos y executor
        database = AppDatabase.getDatabase(this);
        executor = Executors.newSingleThreadExecutor();
    }

    private void inicializarComponentes() {
        txtUser = findViewById(R.id.txtuser);
        txtEmail = findViewById(R.id.txtemail);
        txtTelefono = findViewById(R.id.txttelefono);
        txtContrasenia = findViewById(R.id.txtcontrasenia);
        txtContraseniaConfirm = findViewById(R.id.txtcontraseniaConfirm);
        btnRegistrar = findViewById(R.id.btnregistrar);
        btnRegresar = findViewById(R.id.btnregresar);
    }

    private void configurarEventos() {
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void registrarUsuario() {
        String nombre = txtUser.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String contrasenia = txtContrasenia.getText().toString();
        String confirmarContrasenia = txtContraseniaConfirm.getText().toString();

        // Validar campos
        if (!validarCampos(nombre, email, telefono, contrasenia, confirmarContrasenia)) {
            return;
        }

        // Crear usuario
        Usuario nuevoUsuario = new Usuario(
                nombre,
                email,
                contrasenia,
                telefono,
                "", // null por defecto, no esta incluida en el formulario de registro
                System.currentTimeMillis() // Fecha de registro (timestamp actual)
        );

        // Registrar en base de datos usando ExecutorService
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Verificar si el email ya existe
                    Usuario usuarioExistente = database.usuarioDao().getUsuarioByEmail(email);
                    if (usuarioExistente != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mostrarMensaje("El correo ya está registrado");
                            }
                        });
                        return;
                    }

                    // Verificar si el nombre ya existe
                    Usuario nombreExistente = database.usuarioDao().getUsuarioByName(nombre);
                    if (nombreExistente != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mostrarMensaje("El nombre de usuario ya está en uso");
                            }
                        });
                        return;
                    }

                    // Insertar usuario
                    long id = database.usuarioDao().insertUsuario(nuevoUsuario);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (id > 0) {
                                mostrarMensaje("Usuario registrado exitosamente");
                                limpiarCampos();
                                // Opcional: volver al login
                                finish();
                            } else {
                                mostrarMensaje("Error al registrar usuario");
                            }
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mostrarMensaje("Error: " + e.getMessage());
                        }
                    });
                }
            }
        });
    }

    private boolean validarCampos(String nombre, String email, String telefono, String contrasenia, String confirmarContrasenia) {
        // Validar nombre
        if (nombre.isEmpty()) {
            txtUser.setError("Ingrese su nombre completo");
            txtUser.requestFocus();
            return false;
        }

        if (nombre.length() < 2) {
            txtUser.setError("El nombre debe tener al menos 2 caracteres");
            txtUser.requestFocus();
            return false;
        }

        // Validar email
        if (email.isEmpty()) {
            txtEmail.setError("Ingrese su correo electrónico");
            txtEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError("Ingrese un correo electrónico válido");
            txtEmail.requestFocus();
            return false;
        }

        // Validar teléfono
        if (telefono.isEmpty()) {
            txtTelefono.setError("Ingrese su número de teléfono");
            txtTelefono.requestFocus();
            return false;
        }

        if (telefono.length() < 8) {
            txtTelefono.setError("El teléfono debe tener al menos 8 dígitos");
            txtTelefono.requestFocus();
            return false;
        }

        // Validar contraseña
        if (contrasenia.isEmpty()) {
            txtContrasenia.setError("Ingrese una contraseña");
            txtContrasenia.requestFocus();
            return false;
        }

        if (contrasenia.length() < 6) {
            txtContrasenia.setError("La contraseña debe tener al menos 6 caracteres");
            txtContrasenia.requestFocus();
            return false;
        }

        // Validar confirmación de contraseña
        if (confirmarContrasenia.isEmpty()) {
            txtContraseniaConfirm.setError("Confirme su contraseña");
            txtContraseniaConfirm.requestFocus();
            return false;
        }

        if (!contrasenia.equals(confirmarContrasenia)) {
            txtContraseniaConfirm.setError("Las contraseñas no coinciden");
            txtContraseniaConfirm.requestFocus();
            return false;
        }

        return true;
    }

    private void limpiarCampos() {
        txtUser.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtContrasenia.setText("");
        txtContraseniaConfirm.setText("");
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}