package main.java.com.lab04.Ejercicio2;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CarritoCompra {
    private List<ItemCarrito> items;
    private ServicioPrecio servicioPrecio;
    private double total;
    private List<String> historialOperaciones;

    public CarritoCompra(int capacidad, ServicioPrecio servicioPrecio) {
        this.items = new ArrayList<>();
        this.servicioPrecio = servicioPrecio;
        this.total = 0.0;
        this.historialOperaciones = new ArrayList<>();
        registrarOperacion("HISTORIAL DE OPERACIONES:");
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
        registrarOperacion("Se agrego " + cantidad + " de " + producto.getNombre());
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
        registrarOperacion("Se actualizo la cantidad de " + producto.getNombre() + " a " + nuevaCantidad);
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
        registrarOperacion("Se elimino " + producto.getNombre());
    }

    public void vaciarCarrito() {
        this.items.clear();
        registrarOperacion("Se vacio el carrito");
    }

    public double calcularPrecioProductos() {
        double total = 0.0;
        for (ItemCarrito item : items) {
            total += item.getSubtotal();
        }
        return total;
    }
    public double calcularPrecioTotal() {
        double precioProductos = calcularPrecioProductos();
        double descuento = this.servicioPrecio.calcularDescuento(precioProductos);
        double precioFinal = precioProductos - descuento;
        double impuesto = servicioPrecio.calcularImpuesto(precioFinal);
        total = precioFinal + impuesto;

        registrarOperacion("Se calculo el precio total: " + total);
        return total;
    }
    public void obtenerResumenCompra(){
        System.out.println("Resumen de productos:");
        for (ItemCarrito item : items) {
            System.out.println("- Producto: " + item.getProducto().getNombre() + ", Cantidad: " + item.getCantidad() + ", Precio: " + item.getProducto().getPrecio() + "\n");
        }
        registrarOperacion("Se obtuvo el resumen de productos");
    }

    public List<String> getHistorialOperaciones() {
        return historialOperaciones;
    }

    public double calcularDescuento() {
        return servicioPrecio.calcularDescuento(calcularSubtotal());
    }

    public double calcularImpuesto() {
        return servicioPrecio.calcularImpuesto(calcularSubtotal());
    }

    private void registrarOperacion(String operacion) {
        this.historialOperaciones.add(LocalDateTime.now() + ": " + operacion + "\n");
    }

}
