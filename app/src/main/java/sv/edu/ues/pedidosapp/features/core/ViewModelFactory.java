package sv.edu.ues.pedidosapp.features.core;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import sv.edu.ues.pedidosapp.features.auth.viewmodel.AuthViewModel;
import sv.edu.ues.pedidosapp.features.productos.viewmodel.ProductoViewModel;
import sv.edu.ues.pedidosapp.features.pedidos.viewmodel.PedidoViewModel;
import sv.edu.ues.pedidosapp.features.configuracion.viewmodel.ConfiguracionViewModel;
import sv.edu.ues.pedidosapp.features.detallepedido.viewmodel.DetallePedidoViewModel;
import sv.edu.ues.pedidosapp.features.carrito.viewmodel.CarritoViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;

    public ViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AuthViewModel.class)) {
            return (T) new AuthViewModel(application);
        } else if (modelClass.isAssignableFrom(ProductoViewModel.class)) {
            return (T) new ProductoViewModel(application);
        } else if (modelClass.isAssignableFrom(PedidoViewModel.class)) {
            return (T) new PedidoViewModel(application);
        } else if (modelClass.isAssignableFrom(ConfiguracionViewModel.class)) {
            return (T) new ConfiguracionViewModel(application);
        } else if (modelClass.isAssignableFrom(DetallePedidoViewModel.class)) {
            return (T) new DetallePedidoViewModel(application);
        } else if (modelClass.isAssignableFrom(CarritoViewModel.class)) {
            return (T) new CarritoViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}