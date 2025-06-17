package sv.edu.ues.pedidosapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity_test extends AppCompatActivity {

    private TextView txtBienvenida;
    private Button btnCerrarSesion;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_test);

        sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);

        txtBienvenida = findViewById(R.id.txtBienvenida);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Mostrar datos del usuario
        String nombreUsuario = sharedPreferences.getString("usuario_nombre", "Usuario");
        txtBienvenida.setText("Bienvenido, " + nombreUsuario);

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.cerrarSesion(DashboardActivity_test.this);
            }
        });
    }
}