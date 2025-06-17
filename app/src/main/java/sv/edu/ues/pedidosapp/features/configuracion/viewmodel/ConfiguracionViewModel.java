package sv.edu.ues.pedidosapp.features.configuracion.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import sv.edu.ues.pedidosapp.utils.Constants;

public class ConfiguracionViewModel extends AndroidViewModel {

    private final MutableLiveData<String> themeMode = new MutableLiveData<>();
    private final SharedPreferences sharedPreferences;

    public ConfiguracionViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        loadThemeMode();
    }

    // LiveData para observar el tema
    public LiveData<String> getThemeMode() {
        return themeMode;
    }

    // Cargar el tema desde SharedPreferences
    public void loadThemeMode() {
        String savedTheme = sharedPreferences.getString(Constants.PREF_THEME_MODE, Constants.THEME_SYSTEM);
        themeMode.setValue(savedTheme);
    }

    // Guardar el tema en SharedPreferences
    public void saveThemeMode(String theme) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PREF_THEME_MODE, theme);
        editor.apply();
        themeMode.setValue(theme);
    }
}