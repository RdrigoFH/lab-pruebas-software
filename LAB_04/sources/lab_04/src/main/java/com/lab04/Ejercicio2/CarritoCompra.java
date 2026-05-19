package main.java.com.lab04.Ejercicio2;

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
        ItemCarrito item = new ItemCarrito(producto, cantidad);
        this.items.add(item);
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
        
    }

}
