package main.java.com.lab04.Ejercicio2;

public class CarritoCompra {
    private List<ItemCarrito> items;

    public CarritoCompra(int capacidad) {
        this.items = new ArrayList<>();
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
    
    public double calcularTotal() {
        return 0.0;
    }
    public void obtenerResumen(){
        
    }

}
