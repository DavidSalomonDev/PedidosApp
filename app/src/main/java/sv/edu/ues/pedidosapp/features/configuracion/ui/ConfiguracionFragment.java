package sv.edu.ues.pedidosapp.features.configuracion.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.utils.Constants;
import sv.edu.ues.pedidosapp.utils.SessionManager;

public class ConfiguracionFragment extends Fragment {

    private SessionManager sessionManager;
    private RadioGroup themeRadioGroup;
    private RadioButton lightThemeRadioButton, darkThemeRadioButton, systemThemeRadioButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_configuracion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());

        themeRadioGroup = view.findViewById(R.id.theme_radio_group);
        lightThemeRadioButton = view.findViewById(R.id.light_theme_radio_button);
        darkThemeRadioButton = view.findViewById(R.id.dark_theme_radio_button);
        systemThemeRadioButton = view.findViewById(R.id.system_theme_radio_button);

        // Cargar tema actual y marcar el RadioButton correspondiente
        loadCurrentTheme();

        // Listener para cambios en el tema
        themeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedTheme = Constants.THEME_SYSTEM;
            if (checkedId == R.id.light_theme_radio_button) {
                selectedTheme = Constants.THEME_LIGHT;
            } else if (checkedId == R.id.dark_theme_radio_button) {
                selectedTheme = Constants.THEME_DARK;
            }

            String currentTheme = sessionManager.getThemeMode();

            // Solo guardar y recrear si el tema realmente cambió
            if (!selectedTheme.equals(currentTheme)) {
                sessionManager.setThemeMode(selectedTheme);

                // Guardar flag para volver a configuración tras recreate
                SharedPreferences prefs = requireActivity().getSharedPreferences("config_nav", Context.MODE_PRIVATE);
                prefs.edit().putBoolean("open_config", true).commit(); // Usar commit() en vez de apply()

                requireActivity().recreate();
            }
        });
    }

    private void loadCurrentTheme() {
        String currentTheme = sessionManager.getThemeMode();

        // Quitar listener temporalmente para evitar que se dispare al marcar
        themeRadioGroup.setOnCheckedChangeListener(null);

        switch (currentTheme) {
            case Constants.THEME_LIGHT:
                lightThemeRadioButton.setChecked(true);
                break;
            case Constants.THEME_DARK:
                darkThemeRadioButton.setChecked(true);
                break;
            default:
                systemThemeRadioButton.setChecked(true);
                break;
        }
    }
}