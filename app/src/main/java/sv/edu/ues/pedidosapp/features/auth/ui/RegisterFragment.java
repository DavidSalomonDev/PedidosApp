package sv.edu.ues.pedidosapp.features.auth.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import sv.edu.ues.pedidosapp.MainActivity;
import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.data.local.entity.Usuario;
import sv.edu.ues.pedidosapp.features.auth.viewmodel.AuthViewModel;
import sv.edu.ues.pedidosapp.features.core.ViewModelFactory;
import sv.edu.ues.pedidosapp.features.productos.ui.CatalogoFragment;
import sv.edu.ues.pedidosapp.utils.SessionManager;

public class RegisterFragment extends Fragment {

    private AuthViewModel authViewModel;
    private EditText nombreEditText, emailEditText, passwordEditText, telefonoEditText, direccionEditText;

    private TextView loginTextView;
    private Button registerButton;
    private ProgressBar progressBar;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeComponents(view);
        setupClickListeners();
        observeViewModel();
    }

    private void initializeComponents(View view) {
        // Inicializar SessionManager
        sessionManager = new SessionManager(requireContext());

        // Inicializar ViewModel
        try {
            ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
            authViewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al inicializar la aplicación", Toast.LENGTH_SHORT).show();
            return;
        }

        // Inicializar vistas
        nombreEditText = view.findViewById(R.id.register_nombre_edit_text);
        emailEditText = view.findViewById(R.id.register_email_edit_text);
        passwordEditText = view.findViewById(R.id.register_password_edit_text);
        telefonoEditText = view.findViewById(R.id.register_telefono_edit_text);
        direccionEditText = view.findViewById(R.id.register_direccion_edit_text);
        registerButton = view.findViewById(R.id.register_button);
        loginTextView = view.findViewById(R.id.login_text_view);

        // Inicializar ProgressBar (si existe en el layout)
        progressBar = view.findViewById(R.id.register_progress_bar);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setupClickListeners() {
        registerButton.setOnClickListener(v -> {
            if (validateInputs()) {
                performRegistration();
            }
        });
    }

    private boolean validateInputs() {
        String nombre = nombreEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String telefono = telefonoEditText.getText().toString().trim();
        String direccion = direccionEditText.getText().toString().trim();

        // Validar campos vacíos
        if (TextUtils.isEmpty(nombre)) {
            nombreEditText.setError("El nombre es requerido");
            nombreEditText.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("El email es requerido");
            emailEditText.requestFocus();
            return false;
        }

        // Validar formato de email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Ingrese un email válido");
            emailEditText.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("La contraseña es requerida");
            passwordEditText.requestFocus();
            return false;
        }

        // Validar longitud de contraseña
        if (password.length() < 6) {
            passwordEditText.setError("La contraseña debe tener al menos 6 caracteres");
            passwordEditText.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(telefono)) {
            telefonoEditText.setError("El teléfono es requerido");
            telefonoEditText.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(direccion)) {
            direccionEditText.setError("La dirección es requerida");
            direccionEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void performRegistration() {
        // Mostrar loading
        setLoadingState(true);

        String nombre = nombreEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String telefono = telefonoEditText.getText().toString().trim();
        String direccion = direccionEditText.getText().toString().trim();

        try {
            // Crear usuario
            Usuario usuario = new Usuario(nombre, email, password, telefono, direccion, System.currentTimeMillis());

            // Registrar usuario
            authViewModel.registrarUsuario(usuario);
        } catch (Exception e) {
            setLoadingState(false);
            Toast.makeText(getContext(), "Error al crear usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void observeViewModel() {
        authViewModel.getAuthResult().observe(getViewLifecycleOwner(), authResult -> {
            setLoadingState(false);

            if (authResult != null) {
                if (authResult.isSuccess()) {
                    // Crear usuario completo para la sesión
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(authResult.getUserId());
                    usuario.setNombre(nombreEditText.getText().toString().trim());
                    usuario.setEmail(emailEditText.getText().toString().trim());
                    usuario.setTelefono(telefonoEditText.getText().toString().trim());
                    usuario.setDireccion(direccionEditText.getText().toString().trim());

                    // Guardar sesión
                    sessionManager.createLoginSession(usuario);

                    Toast.makeText(getContext(), authResult.getMessage(), Toast.LENGTH_SHORT).show();

                    // Navegar al catálogo de forma segura
                    navigateToCatalog();
                } else {
                    Toast.makeText(getContext(), authResult.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setLoadingState(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
        registerButton.setEnabled(!isLoading);
        registerButton.setText(isLoading ? "Registrando..." : "Registrarse");
    }

    private void setupClickListeners2() {
        registerButton.setOnClickListener(v -> {
            if (validateInputs()) {
                performRegistration();
            }
        });

        loginTextView.setOnClickListener(v -> {
            try {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new LoginFragment()); // Verifica que este ID sea correcto
                transaction.addToBackStack(null);
                transaction.commit();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error al navegar al login", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void navigateToCatalog() {
        try {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).displayFragment(new CatalogoFragment());
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al navegar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpiar referencias para evitar memory leaks
        authViewModel = null;
        sessionManager = null;
    }
}