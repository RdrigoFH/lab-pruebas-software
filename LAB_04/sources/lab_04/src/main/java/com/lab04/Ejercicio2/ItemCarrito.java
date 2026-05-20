package main.java.com.lab04.Ejercicio2;

import java.time.LocalDateTime;

public class ItemCarrito {
    private Producto producto;
    private int cantidad;
    private final LocalDateTime fecha;


    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.fecha = LocalDateTime.now();
    }

    public Producto getProducto() {
        return producto;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        this.cantidad = cantidad;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }

    public double calcularSubtotalProducto() {
        return producto.getPrecio() * cantidad;
    }
}
