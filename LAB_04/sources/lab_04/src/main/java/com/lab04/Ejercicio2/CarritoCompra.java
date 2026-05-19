package main.java.com.lab04.Ejercicio2;

import java.util.ArrayList;
import java.util.List;

public class CarritoCompra {
    private List<ItemCarrito> items;
    private ServicioPrecio servicioPrecio;
    private double total;

    public CarritoCompra(int capacidad) {
        this.items = new ArrayList<>();
        this.servicioPrecio = new ServicioPrecioImpl(); 
        this.total = 0.0;
    }
    public void agregarProducto(Producto producto, int cantidad) {
        try {
            cantidadValida(cantidad);
            productoDisponible(producto);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
            return;
        }

        ItemCarrito item = new ItemCarrito(producto, cantidad);
        this.items.add(item);
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
    }
    public void eliminarProducto(Producto producto) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getId() == producto.getId()) {
                items.remove(item);
                break;
            }
        }
    }

    public void vaciarCarrito() {
        this.items.clear();
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
        return precioFinal + impuesto;
    }
    public void obtenerResumen(){
        System.out.println("Resumen de productos:");
        for (ItemCarrito item : items) {
            System.out.println("Producto: " + item.getProducto().getNombre() + ", Cantidad: " + item.getCantidad() + ", Precio: " + item.getProducto().getPrecio());
        }
    }

}
