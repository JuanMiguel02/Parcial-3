package org.demo.Models;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Representa una venta realizada en el sistema.
 * Contiene la información del cliente, producto, cantidad, precio y total,
 * además de generar automáticamente un identificador único y registrar la fecha de la transacción.
 */
public class Venta {
    private String id;
    private Cliente cliente;
    private Producto producto;
    private double precioUnitario;
    private LocalDateTime fecha;
    private int cantidad;
    private double total;

    /**
     * Crea una nueva venta asociando un cliente y un producto.
     * Calcula el total de la venta, genera un identificador único y descuenta la cantidad vendida del producto.
     *
     * @param cliente  cliente que realiza la compra.
     * @param producto producto vendido.
     * @param cantidad cantidad de unidades vendidas.
     */
    public Venta(Cliente cliente, Producto producto, int cantidad) {
        this.cliente = cliente;
        this.precioUnitario = producto.getPrecio();
        this.cantidad = cantidad;
        this.producto = producto;
        this.total = calcularTotal();
        this.fecha = LocalDateTime.now();
        this.id = generarIdVenta();

        producto.setCantidad(producto.getCantidad() - cantidad);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    /**
     * Calcula el total de la venta multiplicando el precio unitario por la cantidad.
     * Lanza una excepción si no hay stock suficiente del producto.
     *
     * @return total calculado.
     * @throws IllegalArgumentException si la cantidad solicitada excede el stock disponible.
     */
    private double calcularTotal(){
        if(this.cantidad > producto.getCantidad()){
            throw new IllegalArgumentException("No hay cantidad suficiente del producto: " + producto.getNombre());
        }
        return precioUnitario * this.cantidad;
    }

    /**
     * Genera un identificador único para la venta combinando la marca de tiempo actual y un número aleatorio.
     *
     * @return identificador único con el formato "VENTA-{timestamp}-{número}".
     */
    private static String generarIdVenta(){
        return "VENTA-" + System.currentTimeMillis() + "-" + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    @Override
    public String toString() {
        return
                "id: " + id + '\'' +
                ", cliente: " + cliente.getNombre() +
                ", producto: " + producto +
                ", precioUnitario: " + precioUnitario +
                ", fecha: " + fecha +
                ", cantidad: " + cantidad +
                ", total: " + total + '\n';
    }
}
