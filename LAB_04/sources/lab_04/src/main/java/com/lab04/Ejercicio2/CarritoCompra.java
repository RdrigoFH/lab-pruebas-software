package main.java.com.lab04.Ejercicio2;

import java.util.ArrayList;
import java.util.List;

public class CarritoCompra {
    private List<ItemCarrito> items;
    private ServicioPrecio servicioPrecio;
    private double total;
    private String historialOperaciones;
    private static final double INTERES = 0.18;
    private static final double DESCUENTO = 0.10;

    public CarritoCompra(int capacidad) {
        this.items = new ArrayList<>();
        this.servicioPrecio = new ServicioPrecioImpl(); 
        this.total = 0.0;
        this.historialOperaciones = "Historial de operaciones:\n";
    }
    public void agregarProducto(Producto producto, int cantidad) {
        try {
            cantidadValida(cantidad);
            productoDisponible(producto);
            esDuplicado(producto);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
            return;
        }

        ItemCarrito item = new ItemCarrito(producto, cantidad);
        this.items.add(item);
        this.historialOperaciones += "- Se agrego " + cantidad + " de " + producto.getNombre() + "\n";
    }

    private void cantidadValida(int cantidad) {
        if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad tiene que ser mayor a cero");
        }
    }
    private void productoDisponible(Producto producto) {
        if (!producto.getDisponibilidad()) {
            throw new IllegalStateException("El producto no esta disponible");
        }
    }

    public void actualizarCantidad(Producto producto, int nuevaCantidad) {
        cantidadValida(nuevaCantidad);
        for (ItemCarrito item : items) {
            if (item.getProducto().getId() == producto.getId()) {
                item.setCantidad(nuevaCantidad);
                break;
            }
        }
        
        this.historialOperaciones += "- Se actualizo la cantidad de " + producto.getNombre() + " a " + nuevaCantidad + "\n";
    }
    private void esDuplicado(Producto producto) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getId() == producto.getId()) {
                throw new IllegalArgumentException("El producto ya esta en el carrito");
            }
        }
    }
    public void eliminarProducto(Producto producto) {
        items.removeIf(item -> item.getProducto().getId() == producto.getId());
        this.historialOperaciones += "- Se elimino " + producto.getNombre() + "\n";
    }

    public void vaciarCarrito() {
        this.items.clear();
        this.historialOperaciones += "- Se vacio el carrito\n";
    }

    public double calcularPrecioProductos() {
        double total = 0.0;
        for (ItemCarrito item : items) {
            total += item.getProducto().getPrecio() * item.getCantidad();
        }
        return total;
    }
    public double calcularPrecioTotal() {
        double precioProductos = calcularPrecioProductos();
        double descuento = servicioPrecio.calcularDescuento(precioProductos);
        double precioFinal = precioProductos - descuento;
        double impuesto = servicioPrecio.calcularImpuesto(precioFinal);
        total = precioFinal + impuesto;

        this.historialOperaciones += "- Se calculo el precio total: " + total + "\n";
        return total;
    }
    public void obtenerResumenCompra(){
        System.out.println("Resumen de productos:");
        for (ItemCarrito item : items) {
            System.out.println("Producto: " + item.getProducto().getNombre() + ", Cantidad: " + item.getCantidad() + ", Precio: " + item.getProducto().getPrecio());
        }
        this.historialOperaciones += "- Se obtuvo el resumen de productos\n";
    }

    public String getHistorialOperaciones() {
        return historialOperaciones;
    }

    @Override
    double calcularDescuento(double monto){
        return monto * DESCUENTO;
    }
    double calcularImpuesto(double monto){
        return monto * INTERES;
    }

}
