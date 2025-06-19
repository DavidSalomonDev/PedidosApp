package sv.edu.ues.pedidosapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;

import sv.edu.ues.pedidosapp.data.local.AppDatabase;
import sv.edu.ues.pedidosapp.data.local.SeedDataHelper;
import sv.edu.ues.pedidosapp.features.auth.ui.LoginFragment;
import sv.edu.ues.pedidosapp.features.configuracion.ui.ConfiguracionFragment;
import sv.edu.ues.pedidosapp.features.configuracion.viewmodel.ConfiguracionViewModel;
import sv.edu.ues.pedidosapp.features.core.ViewModelFactory;
import sv.edu.ues.pedidosapp.features.pedidos.ui.ListaPedidosFragment;
import sv.edu.ues.pedidosapp.features.productos.ui.CatalogoFragment;
import sv.edu.ues.pedidosapp.utils.Constants;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView headerUsername, headerEmail;
    private SharedPreferences sharedPreferences;
    private ConfiguracionViewModel configuracionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);

        // Inicializar ViewModel
        ViewModelFactory factory = new ViewModelFactory(getApplication());
        configuracionViewModel = new ViewModelProvider(this, factory).get(ConfiguracionViewModel.class);

        // Inicializar vistas
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Configurar Navigation Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Obtener referencias a las vistas del header
        View headerView = navigationView.getHeaderView(0);
        headerUsername = headerView.findViewById(R.id.header_username);
        headerEmail = headerView.findViewById(R.id.header_email);

        // Cargar datos del usuario desde SharedPreferences
        loadUserData();

        // Cargar tema inicial
        loadTheme();

        // Observar cambios en el tema
        configuracionViewModel.getThemeMode().observe(this, this::setThemeMode);

        // Mostrar fragmento inicial (Login o Catálogo)
        if (isLoggedIn()) {
            displayFragment(new CatalogoFragment());
            navigationView.setCheckedItem(R.id.nav_catalogo);
        } else {
            displayFragment(new LoginFragment());
            navigationView.setCheckedItem(R.id.nav_login);
        }

        initializeSeedData();
    }

    private void initializeSeedData() {
        AppDatabase database = AppDatabase.getInstance(this);
        SeedDataHelper.insertSeedData(database);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            displayFragment(new ConfiguracionFragment());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_catalogo) {
            displayFragment(new CatalogoFragment());
        } else if (id == R.id.nav_pedidos) {
            displayFragment(new ListaPedidosFragment());
        } else if (id == R.id.nav_configuracion) {
            displayFragment(new ConfiguracionFragment());
        } else if (id == R.id.nav_login) {
            // Cerrar sesión
            logout();
            displayFragment(new LoginFragment());
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // Método para mostrar un fragmento
    public void displayFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    // Método para cargar datos del usuario desde SharedPreferences
    private void loadUserData() {
        String username = sharedPreferences.getString(Constants.PREF_USER_EMAIL, "Invitado");
        String email = sharedPreferences.getString(Constants.PREF_USER_EMAIL, "invitado@example.com");

        headerUsername.setText(username);
        headerEmail.setText(email);
    }

    // Método para cerrar sesión
    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.PREF_USER_ID);
        editor.remove(Constants.PREF_USER_EMAIL);
        editor.putBoolean(Constants.PREF_IS_LOGGED_IN, false);
        editor.apply();

        loadUserData(); // Actualizar header
    }

    // Método para cargar el tema desde SharedPreferences
    private void loadTheme() {
        String savedTheme = sharedPreferences.getString(Constants.PREF_THEME_MODE, Constants.THEME_SYSTEM);
        setThemeMode(savedTheme);
    }

    // Método para aplicar el tema
    private void setThemeMode(String theme) {
        switch (theme) {
            case Constants.THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case Constants.THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    // Método para verificar si el usuario está logueado
    private boolean isLoggedIn() {
        return sharedPreferences.getBoolean(Constants.PREF_IS_LOGGED_IN, false);
    }
}