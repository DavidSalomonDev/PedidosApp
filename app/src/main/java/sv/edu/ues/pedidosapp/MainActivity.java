package sv.edu.ues.pedidosapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
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

import java.util.Objects;

import sv.edu.ues.pedidosapp.data.local.AppDatabase;
import sv.edu.ues.pedidosapp.data.local.SeedDataHelper;
import sv.edu.ues.pedidosapp.features.auth.ui.LoginFragment;
import sv.edu.ues.pedidosapp.features.carrito.ui.CarritoFragment;
import sv.edu.ues.pedidosapp.features.configuracion.ui.ConfiguracionFragment;
import sv.edu.ues.pedidosapp.features.configuracion.viewmodel.ConfiguracionViewModel;
import sv.edu.ues.pedidosapp.features.core.ViewModelFactory;
import sv.edu.ues.pedidosapp.features.pedidos.ui.ListaPedidosFragment;
import sv.edu.ues.pedidosapp.features.productos.ui.CatalogoFragment;
import sv.edu.ues.pedidosapp.utils.Constants;
import sv.edu.ues.pedidosapp.utils.SessionManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView headerUsername, headerEmail;
    private SessionManager sessionManager;
    private ConfiguracionViewModel configuracionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // APLICAR EL TEMA ANTES DE setContentView
        sessionManager = new SessionManager(this);
        String savedTheme = sessionManager.getThemeMode();
        setThemeMode(savedTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar ViewModel
        ViewModelFactory factory = new ViewModelFactory(getApplication());
        configuracionViewModel = new ViewModelProvider(this, factory).get(ConfiguracionViewModel.class);

        // Inicializar vistas
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        updateNavigationMenu();

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

        // Cargar datos del usuario desde SessionManager
        loadUserData();

        // ELIMINAR ESTAS LÍNEAS QUE CAUSABAN EL LOOP:
        // loadTheme();
        // configuracionViewModel.getThemeMode().observe(this, this::setThemeMode);

        // Mostrar fragmento inicial (Login o Catálogo)
        if (sessionManager.isLoggedIn()) {
            displayFragment(new CatalogoFragment());
            navigationView.setCheckedItem(R.id.nav_catalogo);
        } else {
            displayFragment(new LoginFragment());
            navigationView.setCheckedItem(R.id.nav_login);
        }

        initializeSeedData();

        SharedPreferences prefs = getSharedPreferences("config_nav", Context.MODE_PRIVATE);
        boolean openConfig = prefs.getBoolean("open_config", false);
        if (openConfig) {
            prefs.edit().putBoolean("open_config", false).commit();
            displayFragment(new ConfiguracionFragment());
            navigationView.setCheckedItem(R.id.nav_configuracion);
        }
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            displayFragment(new ConfiguracionFragment());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_catalogo) {
            displayFragment(new CatalogoFragment());
        } else if (id == R.id.nav_carrito) {
            displayFragment(new CarritoFragment());
        } else if (id == R.id.nav_pedidos) {
            displayFragment(new ListaPedidosFragment());
        } else if (id == R.id.nav_configuracion) {
            displayFragment(new ConfiguracionFragment());
        } else if (id == R.id.nav_logout) {
            // Cerrar sesión
            showLogoutConfirmationDialog();
            displayFragment(new LoginFragment());
            navigationView.setCheckedItem(R.id.nav_login);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutConfirmationDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    logout();
                    displayFragment(new LoginFragment());
                    navigationView.setCheckedItem(R.id.nav_login);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Método para mostrar un fragmento
    public void displayFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void updateNavigationMenu() {
        Menu menu = navigationView.getMenu();
        boolean isLoggedIn = sessionManager.isLoggedIn();

        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);

            if (item.hasSubMenu()) {
                // Submenú (como "Autenticación")
                SubMenu subMenu = item.getSubMenu();
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subItem = subMenu.getItem(j);
                    if (subItem.getItemId() == R.id.nav_login) {
                        subItem.setVisible(!isLoggedIn);
                    }
                }
                // Oculta el grupo si no tiene items visibles
                item.setVisible(!isLoggedIn);
            } else {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_catalogo ||
                        itemId == R.id.nav_carrito ||
                        itemId == R.id.nav_pedidos ||
                        itemId == R.id.nav_configuracion ||
                        itemId == R.id.nav_logout) {
                    item.setVisible(isLoggedIn);
                }
            }
        }
    }

    // Método para cargar datos del usuario desde SessionManager
    public void loadUserData() {
        if (sessionManager.isLoggedIn()) {
            String username = sessionManager.getUserName();
            String email = sessionManager.getUserEmail();

            // AGREGAR LOGS PARA DEBUG
            System.out.println("DEBUG - Usuario logueado: " + username + " | " + email);

            if (username == null || username.trim().isEmpty()) {
                username = email.split("@")[0];
            }

            headerUsername.setText(username);
            headerEmail.setText(email);
        } else {
            System.out.println("DEBUG - Usuario NO logueado");
            headerUsername.setText("Invitado");
            headerEmail.setText("invitado@example.com");
        }
    }

    // Método para cerrar sesión
    private void logout() {
        sessionManager.logout();
        loadUserData();
        updateNavigationMenu();
    }

    // Método para cargar el tema desde SessionManager
    public void loadTheme() {
        String savedTheme = sessionManager.getThemeMode();
        setThemeMode(savedTheme);
    }

    // Método para aplicar el tema
    public void setThemeMode(String theme) {
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

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
        updateNavigationMenu();
    }
}