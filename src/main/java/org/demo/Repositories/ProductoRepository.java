package org.demo.Repositories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.demo.Models.Producto;

import java.util.Optional;


/**
 * Repositorio encargado de gestionar los productos registrados en el sistema.
 * Implementa un patrón Singleton para garantizar una única instancia compartida
 * y utiliza una lista observable para permitir la sincronización con la interfaz gráfica.
 */
public class ProductoRepository {
    private static ProductoRepository instancia;
    private final ObservableList<Producto> productos;


    /**
     * Constructor privado.
     * Inicializa la lista de productos y carga datos de ejemplo.
     */
    private ProductoRepository(){
        productos = FXCollections.observableArrayList();
        cargarDatosEjemplo();
    }

    /**
     * Devuelve la instancia única del repositorio.
     * Si no existe, se crea una nueva.
     *
     * @return instancia única de {@code ProductoRepository}.
     */
    public static ProductoRepository getInstancia(){
        if(instancia == null){
            instancia = new ProductoRepository();
        }
        return instancia;
    }

    /**
     * Retorna la lista observable de productos.
     * Esta lista puede ser usada para enlazar datos con componentes gráficos.
     *
     * @return lista observable de productos.
     */
    public ObservableList<Producto> getProductos(){
        return productos;
    }

    /**
     * Guarda un nuevo producto en el repositorio.
     * Lanza una excepción si ya existe un producto con el mismo nombre.
     *
     * @param producto producto a registrar.
     */
    public void guardarProducto(Producto producto){
        if(existeProductoConNombre(producto.getNombre())){
            throw new RuntimeException("Producto ya existe");
        }
        productos.add(producto);
    }

    /**
     * Elimina un producto existente según su nombre.
     *
     * @param producto producto a eliminar.
     */
    public void eliminarProducto(Producto producto){
        productos.removeIf(p -> p.getNombre().equals(producto.getNombre()));
    }

    /**
     * Actualiza los datos de un producto existente basado en su ID.
     * Si el producto existe, se reemplazan sus valores por los del objeto recibido.
     *
     * @param producto producto con los datos actualizados.
     */
    public void actualizarProducto(Producto producto){
        Optional<Producto> productoExistenteOpt = buscarProductoPorId(producto.getId());
        if(productoExistenteOpt.isPresent()){
            Producto productoExistente = productoExistenteOpt.get();

            productoExistente.setNombre(producto.getNombre());
            productoExistente.setPrecio(producto.getPrecio());
            productoExistente.setCategoria(producto.getCategoria());

        }
    }

    /**
     * Verifica si existe un producto con un nombre determinado.
     *
     * @param nombre nombre del producto a verificar.
     * @return {@code true} si el producto ya existe, {@code false} en caso contrario.
     */
    public boolean existeProductoConNombre(String nombre){
        return productos.stream()
                .anyMatch(producto -> producto.getNombre().equals(nombre));
    }

    /**
     * Busca un producto por su identificador único.
     *
     * @param id identificador del producto.
     * @return un {@code Optional<Producto>} que contiene el producto si se encuentra.
     */
    private Optional<Producto> buscarProductoPorId(int id){
        return productos.stream()
                .filter(producto -> producto.getId() == id).
                findFirst();
    }

    /**
     * Carga productos de ejemplo al iniciar el repositorio.
     * Los datos son utilizados con fines demostrativos.
     */
    private void cargarDatosEjemplo(){
        Producto p1 = new Producto(2000, "Coca Cola", 3, "Bebida");
        Producto p2 = new Producto(4500, "Detódito", 5, "Mecato");
        Producto p3 = new Producto(3000, "Chocorramo", 4, "Mecato");

        guardarProducto(p1);
        guardarProducto(p2);
        guardarProducto(p3);
    }
}
