package sv.edu.ues.pedidosapp.data.repository;

import android.app.Application;

import sv.edu.ues.pedidosapp.data.local.AppDatabase;

public abstract class BaseRepository {

    protected AppDatabase database;

    public BaseRepository(Application application) {
        database = AppDatabase.getInstance(application);
    }

    // Método para obtener la base de datos (útil para clases hijas)
    protected AppDatabase getDatabase() {
        return database;
    }
}