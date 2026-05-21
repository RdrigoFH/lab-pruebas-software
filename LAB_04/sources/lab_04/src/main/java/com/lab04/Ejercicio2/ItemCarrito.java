package main.java.com.lab04.Ejercicio2;

public class ItemCarrito {
    private Producto producto;
    private int cantidad;


    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
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

    public double calcularSubtotal() {
        return producto.getPrecio() * cantidad;
    }
}
