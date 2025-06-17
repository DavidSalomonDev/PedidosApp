package sv.edu.ues.pedidosapp.features.core;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import sv.edu.ues.pedidosapp.data.repository.RepositoryManager;

public class BaseViewModel extends AndroidViewModel {

    protected RepositoryManager repositoryManager;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        repositoryManager = RepositoryManager.getInstance(application);
    }

    // Método para obtener el RepositoryManager (útil para clases hijas)
    protected RepositoryManager getRepositoryManager() {
        return repositoryManager;
    }
}