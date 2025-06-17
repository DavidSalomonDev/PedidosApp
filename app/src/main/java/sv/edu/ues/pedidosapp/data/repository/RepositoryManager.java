package sv.edu.ues.pedidosapp.data.repository;

import android.app.Application;

public class RepositoryManager {

    private static RepositoryManager instance;

    private UsuarioRepository usuarioRepository;
    private ProductoRepository productoRepository;
    private PedidoRepository pedidoRepository;
    private DetallePedidoRepository detallePedidoRepository;

    private RepositoryManager(Application application) {
        usuarioRepository = new UsuarioRepository(application);
        productoRepository = new ProductoRepository(application);
        pedidoRepository = new PedidoRepository(application);
        detallePedidoRepository = new DetallePedidoRepository(application);
    }

    public static synchronized RepositoryManager getInstance(Application application) {
        if (instance == null) {
            instance = new RepositoryManager(application);
        }
        return instance;
    }

    // Getters para cada repositorio
    public UsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }

    public ProductoRepository getProductoRepository() {
        return productoRepository;
    }

    public PedidoRepository getPedidoRepository() {
        return pedidoRepository;
    }

    public DetallePedidoRepository getDetallePedidoRepository() {
        return detallePedidoRepository;
    }
}