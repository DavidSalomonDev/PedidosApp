package sv.edu.ues.pedidosapp.data.local;

import sv.edu.ues.pedidosapp.data.local.dao.ProductoDao;
import sv.edu.ues.pedidosapp.data.local.dao.UsuarioDao;
import sv.edu.ues.pedidosapp.data.local.entity.Producto;
import sv.edu.ues.pedidosapp.data.local.entity.Usuario;

public class SeedDataHelper {

    public static void insertSeedData(AppDatabase database) {
        ProductoDao productoDao = database.productoDao();
        UsuarioDao usuarioDao = database.usuarioDao();

        // Verificar si ya existen datos
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Solo insertar si no hay productos
            if (productoDao.getProductoCount() == 0) {
                insertProducts(productoDao);
            }

            // Solo insertar si no hay usuarios
            if (usuarioDao.getUserCount() == 0) {
                insertUsers(usuarioDao);
            }
        });
    }

    private static void insertProducts(ProductoDao productoDao) {
        // Hamburguesas
        productoDao.insertProducto(createProduct("Hamburguesa Clásica", "Hamburguesa con carne, lechuga, tomate y queso", 8.99, "Hamburguesas"));
        productoDao.insertProducto(createProduct("Hamburguesa BBQ", "Hamburguesa con carne, salsa BBQ, cebolla y queso", 10.99, "Hamburguesas"));
        productoDao.insertProducto(createProduct("Hamburguesa Doble", "Doble carne, doble queso, lechuga y tomate", 12.99, "Hamburguesas"));
        productoDao.insertProducto(createProduct("Hamburguesa Vegetariana", "Hamburguesa vegetal con aguacate y vegetales frescos", 9.99, "Hamburguesas"));

        // Pizza
        productoDao.insertProducto(createProduct("Pizza Margherita", "Pizza con salsa de tomate, mozzarella y albahaca", 14.99, "Pizza"));
        productoDao.insertProducto(createProduct("Pizza Pepperoni", "Pizza con pepperoni y mozzarella", 16.99, "Pizza"));
        productoDao.insertProducto(createProduct("Pizza Hawaiana", "Pizza con jamón, piña y mozzarella", 15.99, "Pizza"));
        productoDao.insertProducto(createProduct("Pizza Suprema", "Pizza con pepperoni, salchicha, pimientos y cebolla", 18.99, "Pizza"));

        // Pollo
        productoDao.insertProducto(createProduct("Pollo Frito Original", "Piezas de pollo frito con receta secreta", 11.99, "Pollo"));
        productoDao.insertProducto(createProduct("Pollo Picante", "Piezas de pollo frito con especias picantes", 12.99, "Pollo"));
        productoDao.insertProducto(createProduct("Alitas BBQ", "Alitas de pollo con salsa BBQ", 9.99, "Pollo"));
        productoDao.insertProducto(createProduct("Nuggets de Pollo", "Nuggets crujientes con salsa a elección", 7.99, "Pollo"));

        // Tacos
        productoDao.insertProducto(createProduct("Tacos de Carne", "Tres tacos con carne asada, cebolla y cilantro", 8.99, "Tacos"));
        productoDao.insertProducto(createProduct("Tacos de Pollo", "Tres tacos con pollo marinado y vegetales", 8.99, "Tacos"));
        productoDao.insertProducto(createProduct("Tacos de Pescado", "Tres tacos con pescado empanizado y salsa", 9.99, "Tacos"));

        // Burritos
        productoDao.insertProducto(createProduct("Burrito de Carne", "Burrito grande con carne, arroz, frijoles y queso", 10.99, "Burritos"));
        productoDao.insertProducto(createProduct("Burrito de Pollo", "Burrito con pollo, arroz, frijoles y vegetales", 10.99, "Burritos"));
        productoDao.insertProducto(createProduct("Burrito Vegetariano", "Burrito con frijoles, arroz, aguacate y vegetales", 9.99, "Burritos"));

        // Bebidas
        productoDao.insertProducto(createProduct("Coca Cola", "Refresco de cola 500ml", 2.99, "Bebidas"));
        productoDao.insertProducto(createProduct("Pepsi", "Refresco de cola 500ml", 2.99, "Bebidas"));
        productoDao.insertProducto(createProduct("Sprite", "Refresco de limón 500ml", 2.99, "Bebidas"));
        productoDao.insertProducto(createProduct("Agua Natural", "Agua embotellada 500ml", 1.99, "Bebidas"));
        productoDao.insertProducto(createProduct("Jugo de Naranja", "Jugo natural de naranja 400ml", 3.99, "Bebidas"));

        // Postres
        productoDao.insertProducto(createProduct("Helado de Vainilla", "Helado cremoso de vainilla", 4.99, "Postres"));
        productoDao.insertProducto(createProduct("Helado de Chocolate", "Helado cremoso de chocolate", 4.99, "Postres"));
        productoDao.insertProducto(createProduct("Pastel de Chocolate", "Rebanada de pastel de chocolate", 5.99, "Postres"));
        productoDao.insertProducto(createProduct("Cheesecake", "Rebanada de cheesecake de fresa", 6.99, "Postres"));
    }

    private static void insertUsers(UsuarioDao usuarioDao) {
        long fechaRegistro = System.currentTimeMillis();

        // Usuario administrador
        usuarioDao.insertUsuario(
                new Usuario(
                        "Administrador",
                        "admin@restaurante.com",
                        "admin123",
                        "7777-7777",
                        "Dirección Admin",
                        fechaRegistro
                )
        );

        // Usuarios de ejemplo
        usuarioDao.insertUsuario(
                new Usuario(
                        "Juan Pérez",
                        "juan@email.com",
                        "password123",
                        "1234-5678",
                        "Colonia Centro",
                        fechaRegistro
                )
        );

        usuarioDao.insertUsuario(
                new Usuario(
                        "María García",
                        "maria@email.com",
                        "password123",
                        "2345-6789",
                        "Colonia Escalón",
                        fechaRegistro
                )
        );

        usuarioDao.insertUsuario(
                new Usuario(
                        "Carlos López",
                        "carlos@email.com",
                        "password123",
                        "3456-7890",
                        "Colonia San Benito",
                        fechaRegistro
                )
        );

        usuarioDao.insertUsuario(
                new Usuario(
                        "Ana Martínez",
                        "ana@email.com",
                        "password123",
                        "4567-8901",
                        "Colonia Médica",
                        fechaRegistro
                )
        );
    }

    private static Producto createProduct(String nombre, String descripcion, double precio, String categoria) {
        return new Producto(nombre, descripcion, precio, categoria, generateImageUrl(nombre), true);
    }

    private static String generateImageUrl(String productName) {
        // Generar URL de imagen basada en el nombre del producto
        String imageName = productName.toLowerCase()
                .replace(" ", "_")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("ñ", "n");

        return "https://via.placeholder.com/300x200/FF6B35/FFFFFF?text=" + imageName;
    }
}