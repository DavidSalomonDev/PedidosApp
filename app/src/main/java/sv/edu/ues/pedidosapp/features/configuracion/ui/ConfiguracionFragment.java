package sv.edu.ues.pedidosapp.features.configuracion.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import sv.edu.ues.pedidosapp.R;
import sv.edu.ues.pedidosapp.features.configuracion.viewmodel.ConfiguracionViewModel;
import sv.edu.ues.pedidosapp.features.core.ViewModelFactory;
import sv.edu.ues.pedidosapp.utils.Constants;

public class ConfiguracionFragment extends Fragment {

    private ConfiguracionViewModel configuracionViewModel;
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

        // Inicializar ViewModel
        ViewModelFactory factory = new ViewModelFactory(getActivity().getApplication());
        configuracionViewModel = new ViewModelProvider(this, factory).get(ConfiguracionViewModel.class);

        // Inicializar vistas
        themeRadioGroup = view.findViewById(R.id.theme_radio_group);
        lightThemeRadioButton = view.findViewById(R.id.light_theme_radio_button);
        darkThemeRadioButton = view.findViewById(R.id.dark_theme_radio_button);
        systemThemeRadioButton = view.findViewById(R.id.system_theme_radio_button);

        // Cargar tema actual
        loadCurrentTheme();

        // Listener para cambios en el tema
        themeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String theme = Constants.THEME_SYSTEM;
            if (checkedId == R.id.light_theme_radio_button) {
                theme = Constants.THEME_LIGHT;
            } else if (checkedId == R.id.dark_theme_radio_button) {
                theme = Constants.THEME_DARK;
            }
            configuracionViewModel.saveThemeMode(theme);
        });
    }

    // Método para cargar el tema actual
    private void loadCurrentTheme() {
        configuracionViewModel.getThemeMode().observe(getViewLifecycleOwner(), theme -> {
            switch (theme) {
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
        });
    }
}