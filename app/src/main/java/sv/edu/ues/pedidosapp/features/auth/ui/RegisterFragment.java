package sv.edu.ues.pedidosapp.features.auth.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import sv.edu.ues.pedidosapp.MainActivity;
import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.data.local.entity.Usuario;
import sv.edu.ues.pedidosapp.features.auth.viewmodel.AuthViewModel;
import sv.edu.ues.pedidosapp.features.core.ViewModelFactory;
import sv.edu.ues.pedidosapp.utils.Constants;

public class RegisterFragment extends Fragment {

    private AuthViewModel authViewModel;
    private EditText nombreEditText, emailEditText, passwordEditText, telefonoEditText, direccionEditText;
    private Button registerButton;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);

        // Inicializar ViewModel
        ViewModelFactory factory = new ViewModelFactory(getActivity().getApplication());
        authViewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);

        // Inicializar vistas
        nombreEditText = view.findViewById(R.id.register_nombre_edit_text);
        emailEditText = view.findViewById(R.id.register_email_edit_text);
        passwordEditText = view.findViewById(R.id.register_password_edit_text);
        telefonoEditText = view.findViewById(R.id.register_telefono_edit_text);
        direccionEditText = view.findViewById(R.id.register_direccion_edit_text);
        registerButton = view.findViewById(R.id.register_button);

        // Listener para el botón de registro
        registerButton.setOnClickListener(v -> {
            String nombre = nombreEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String telefono = telefonoEditText.getText().toString().trim();
            String direccion = direccionEditText.getText().toString().trim();

            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear usuario
            Usuario usuario = new Usuario(nombre, email, password, telefono, direccion, System.currentTimeMillis());

            // Registrar usuario
            authViewModel.registrarUsuario(usuario);
        });

        // Observar el resultado del registro
        authViewModel.getAuthResult().observe(getViewLifecycleOwner(), authResult -> {
            if (authResult.isSuccess()) {
                // Guardar datos del usuario en SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong(Constants.PREF_USER_ID, authResult.getUserId());
                editor.putString(Constants.PREF_USER_EMAIL, emailEditText.getText().toString().trim());
                editor.putBoolean(Constants.PREF_IS_LOGGED_IN, true);
                editor.apply();

                Toast.makeText(getContext(), authResult.getMessage(), Toast.LENGTH_SHORT).show();

                // Navegar al catálogo
                ((MainActivity) getActivity()).displayFragment(new CatalogoFragment());
            } else {
                Toast.makeText(getContext(), authResult.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}