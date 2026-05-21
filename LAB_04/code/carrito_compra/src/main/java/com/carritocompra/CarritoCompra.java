package com.carritocompra;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CarritoCompra {
    private List<ItemCarrito> items;
    private ServicioPrecio servicioPrecio;
    private double total;
    private List<String> historialOperaciones;

    public CarritoCompra(ServicioPrecio servicioPrecio) {
        if (servicioPrecio == null) {
            throw new IllegalArgumentException("El servicio de precios no puede ser nulo");
        }
        this.items = new ArrayList<>();
        this.servicioPrecio = servicioPrecio;
        this.total = 0.0;
        this.historialOperaciones = new ArrayList<>();
        registrarOperacion("HISTORIAL DE OPERACIONES:");
    }

    //Getters
    public List<String> getHistorialOperaciones() {
        return historialOperaciones;
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    public int getCantidadTotalProductos() {
        int cantidadTotal = 0;
        for (ItemCarrito item : items) {
            cantidadTotal += item.getCantidad();
        }
        return cantidadTotal;
    }
    //Metodos

    public void agregarProducto(Producto producto, int cantidad) {
        validarProductoNoNulo(producto);
        cantidadValida(cantidad);
        productoDisponible(producto);

        if (contieneProducto(producto)) {
            actualizarCantidad(producto, cantidad);
        }else {
            ItemCarrito item = new ItemCarrito(producto, cantidad);
            this.items.add(item);
            registrarOperacion("Se agrego " + cantidad + " de " + producto.getNombre());
        }

    }

    public void removerProducto(Producto producto) {
        validarProductoNoNulo(producto);
        boolean eliminado = items.removeIf(item -> item.getProducto().getId().equals(producto.getId()));
        if (eliminado) {
            registrarOperacion("Se elimino " + producto.getNombre());
        } else {
            registrarOperacion("El producto no se encontro en el carrito");
        }
    }

    public void vaciarCarrito() {
        this.items.clear();
        registrarOperacion("Se vacio el carrito");
    }

    public double calcularPrecioProductos() {
        double total = 0.0;
        for (ItemCarrito item : items) {
            total += item.calcularSubtotal();
        }
        return total;
    }
    public double calcularPrecioTotal() {
        double precioProductos = calcularPrecioProductos();
        double descuento = this.servicioPrecio.calcularDescuento(precioProductos);
        double precioFinal = precioProductos - descuento;
        double impuesto = servicioPrecio.calcularImpuesto(precioProductos);
        total = precioFinal + impuesto;

        registrarOperacion("Se calculo el precio total: " + total);
        return total;
    }

    public String obtenerResumenCompra() {
        String resumen = "Resumen de productos:\n";

        for (ItemCarrito item : items) {
            resumen = resumen + "- Producto: " + item.getProducto().getNombre() + ", Cantidad: " + item.getCantidad() + ", Precio: " + item.getProducto().getPrecio() + "\n";
        }

        registrarOperacion("Se obtuvo el resumen de productos");
        return resumen;
    }

    public double calcularDescuento() {
        return servicioPrecio.calcularDescuento(calcularPrecioProductos());
    }

    public double calcularImpuesto() {
        return servicioPrecio.calcularImpuesto(calcularPrecioProductos());
    }

    private void registrarOperacion(String operacion) {
        this.historialOperaciones.add(LocalDateTime.now() + ": " + operacion + "\n");
    }

    // Validaciones
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
            if (item.getProducto().getId().equals(producto.getId())) {
                item.setCantidad(nuevaCantidad);
                break;
            }
        }
        registrarOperacion("Se actualizo la cantidad de " + producto.getNombre() + " a " + nuevaCantidad);
    }

    public boolean contieneProducto(Producto producto) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getId().equals(producto.getId())) {
                return true;
            }
        }
        return false;
    }
    

    private void validarProductoNoNulo(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
    }

}
