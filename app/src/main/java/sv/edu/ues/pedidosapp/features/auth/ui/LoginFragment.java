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
import androidx.navigation.Navigation;

import sv.edu.ues.pedidosapp.MainActivity;
import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.features.auth.viewmodel.AuthViewModel;
import sv.edu.ues.pedidosapp.features.core.ViewModelFactory;
import sv.edu.ues.pedidosapp.features.productos.ui.CatalogoFragment;
import sv.edu.ues.pedidosapp.utils.SessionManager;

public class LoginFragment extends Fragment {

    private AuthViewModel authViewModel;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private ProgressBar progressBar;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeComponents(view);
        checkExistingSession();
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
        emailEditText = view.findViewById(R.id.login_email_edit_text);
        passwordEditText = view.findViewById(R.id.login_password_edit_text);
        loginButton = view.findViewById(R.id.login_button);
        registerTextView = view.findViewById(R.id.register_text_view);

        // Inicializar ProgressBar (si existe en el layout)
        progressBar = view.findViewById(R.id.login_progress_bar);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void checkExistingSession() {
        // Si ya hay una sesión activa, navegar directamente al catálogo
        if (sessionManager.isLoggedIn()) {
            navigateToCatalog();
        }
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> {
            if (validateInputs()) {
                performLogin();
            }
        });

        registerTextView.setOnClickListener(v -> {
            try {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new RegisterFragment()); // Asegúrate del ID correcto del contenedor
                transaction.addToBackStack(null);
                transaction.commit();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error al navegar al registro", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validar email
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("El email es requerido");
            emailEditText.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Ingrese un email válido");
            emailEditText.requestFocus();
            return false;
        }

        // Validar contraseña
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("La contraseña es requerida");
            passwordEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void performLogin() {
        // Mostrar loading
        setLoadingState(true);

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        try {
            authViewModel.login(email, password);
        } catch (Exception e) {
            setLoadingState(false);
            Toast.makeText(getContext(), "Error al iniciar sesión: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void observeViewModel() {
        authViewModel.getAuthResult().observe(getViewLifecycleOwner(), authResult -> {
            setLoadingState(false);

            if (authResult != null) {
                if (authResult.isSuccess()) {
                    // Obtener el usuario completo desde el repositorio
                    obtenerUsuarioCompleto(authResult.getUserId());
                } else {
                    Toast.makeText(getContext(), authResult.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Observar cuando se obtiene el usuario completo
        authViewModel.getUsuarioCompleto().observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null) {
                // Guardar sesión con datos completos
                sessionManager.createLoginSession(usuario);

                Toast.makeText(getContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                // Navegar al catálogo
                navigateToCatalog();
            }
        });
    }

    private void obtenerUsuarioCompleto(long userId) {
        // Solicitar los datos completos del usuario
        authViewModel.obtenerUsuarioPorId(userId);
    }

    private void setLoadingState(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
        loginButton.setEnabled(!isLoading);
        loginButton.setText(isLoading ? "Iniciando sesión..." : "Iniciar Sesión");
        emailEditText.setEnabled(!isLoading);
        passwordEditText.setEnabled(!isLoading);
        registerTextView.setEnabled(!isLoading);
    }

    private void navigateToCatalog() {
        try {
            if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.displayFragment(new CatalogoFragment());
                mainActivity.loadUserData();
                mainActivity.updateNavigationMenu();
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