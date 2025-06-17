package sv.edu.ues.pedidosapp.features.auth.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import sv.edu.ues.pedidosapp.MainActivity;
import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.features.auth.viewmodel.AuthViewModel;
import sv.edu.ues.pedidosapp.features.core.ViewModelFactory;
import sv.edu.ues.pedidosapp.utils.Constants;

public class LoginFragment extends Fragment {

    private AuthViewModel authViewModel;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
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
        emailEditText = view.findViewById(R.id.login_email_edit_text);
        passwordEditText = view.findViewById(R.id.login_password_edit_text);
        loginButton = view.findViewById(R.id.login_button);
        registerTextView = view.findViewById(R.id.register_text_view);

        // Listener para el botón de login
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, ingrese email y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            authViewModel.login(email, password);
        });

        // Listener para el TextView de registro
        registerTextView.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
        });
/*
        // Observar el resultado del login
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
        */

    }
}