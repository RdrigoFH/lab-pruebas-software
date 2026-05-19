package com.lab04;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Representa el carrito de compras de un cliente.
 */
public class CarritoCompra {
  private final List<ItemCarrito> items;
  private final ServicioPrecio servicioPrecio;
  private final List<String> historialOperaciones;

  public CarritoCompra(ServicioPrecio servicioPrecio) {
    if (servicioPrecio == null) {
      throw new IllegalArgumentException("El servicio de precios no puede ser nulo");
    }
    this.items = new ArrayList<>();
    this.servicioPrecio = servicioPrecio;
    this.historialOperaciones = new ArrayList<>();
    registrarOperacion("Carrito creado");
  }

  public List<ItemCarrito> getItems() {
    return Collections.unmodifiableList(items);
  }

  public List<String> getHistorialOperaciones() {
    return Collections.unmodifiableList(historialOperaciones);
  }

  private void registrarOperacion(String operacion) {
    historialOperaciones.add(
        String.format("[%s] %s", LocalDateTime.now(), operacion));
  }

  public void agregarProducto(Producto producto, int cantidad) {
    if (producto == null) {
      throw new IllegalArgumentException("El producto no puede ser nulo");
    }
    if (cantidad <= 0) {
      throw new IllegalArgumentException("La cantidad debe ser positiva");
    }
    if (!producto.isDisponible()) {
      throw new IllegalStateException(
          "No se puede agregar un producto no disponible: " + producto.getNombre());
    }

    Optional<ItemCarrito> itemExistente = items.stream()
        .filter(item -> item.getProducto().equals(producto))
        .findFirst();

    if (itemExistente.isPresent()) {
      ItemCarrito item = itemExistente.get();
      int nuevaCantidad = item.getCantidad() + cantidad;
      item.setCantidad(nuevaCantidad);
      registrarOperacion(String.format(
          "Cantidad actualizada para %s: +%d (nueva cantidad: %d)",
          producto.getNombre(), cantidad, nuevaCantidad));
    } else {
      items.add(new ItemCarrito(producto, cantidad));
      registrarOperacion(String.format(
          "Producto agregado: %s x%d", producto.getNombre(), cantidad));
    }
  }

  public void removerProducto(Producto producto, int cantidad) {
    if (producto == null) {
      throw new IllegalArgumentException("El producto no puede ser nulo");
    }
    if (cantidad <= 0) {
      throw new IllegalArgumentException("La cantidad a remover debe ser positiva");
    }

    Optional<ItemCarrito> itemExistente = items.stream()
        .filter(item -> item.getProducto().equals(producto))
        .findFirst();

    if (itemExistente.isEmpty()) {
      throw new IllegalStateException(
          "El producto no existe en el carrito: " + producto.getNombre());
    }

    ItemCarrito item = itemExistente.get();
    int nuevaCantidad = item.getCantidad() - cantidad;

    if (nuevaCantidad <= 0) {
      items.remove(item);
      registrarOperacion(
          String.format("Producto removido completamente: %s", producto.getNombre()));
    } else {
      item.setCantidad(nuevaCantidad);
      registrarOperacion(String.format(
          "Cantidad reducida para %s: -%d (nueva cantidad: %d)",
          producto.getNombre(), cantidad, nuevaCantidad));
    }
  }

  public void vaciarCarrito() {
    items.clear();
    registrarOperacion("Carrito vaciado completamente");
  }

  public double calcularSubtotal() {
    return items.stream()
        .mapToDouble(ItemCarrito::getSubtotal)
        .sum();
  }

  public double calcularDescuento() {
    double subtotal = calcularSubtotal();
    return servicioPrecio.calcularDescuento(subtotal);
  }

  public double calcularImpuesto() {
    double subtotal = calcularSubtotal();
    return servicioPrecio.calcularImpuesto(subtotal);
  }

  public double calcularTotal() {
    double subtotal = calcularSubtotal();
    double descuento = calcularDescuento();
    double impuesto = calcularImpuesto();
    double total = (subtotal - descuento) + impuesto;

    registrarOperacion(String.format(
        "Total calculado: subtotal=%.2f, descuento=%.2f, impuesto=%.2f, total=%.2f",
        subtotal, descuento, impuesto, total));
    return total;
  }

  public String obtenerResumenCompra() {
    if (items.isEmpty()) {
      return "El carrito está vacío";
    }

    StringBuilder sb = new StringBuilder();
    sb.append("=== RESUMEN DE COMPRA ===\n");
    sb.append("Productos:\n");

    for (ItemCarrito item : items) {
      sb.append(String.format(
          "  - %s: %d x $%.2f = $%.2f\n",
          item.getProducto().getNombre(),
          item.getCantidad(),
          item.getProducto().getPrecio(),
          item.getSubtotal()));
    }

    double subtotal = calcularSubtotal();
    double descuento = calcularDescuento();
    double impuesto = calcularImpuesto();
    double total = (subtotal - descuento) + impuesto;

    sb.append(String.format("\nSubtotal: $%.2f\n", subtotal));
    sb.append(String.format("Descuento: -$%.2f\n", descuento));
    sb.append(String.format("Impuesto: +$%.2f\n", impuesto));
    sb.append(String.format("TOTAL: $%.2f\n", total));

    return sb.toString();
  }

  public int getCantidadTotalProductos() {
    return items.stream()
        .mapToInt(ItemCarrito::getCantidad)
        .sum();
  }

  public boolean contieneProducto(Producto producto) {
    return items.stream()
        .anyMatch(item -> item.getProducto().equals(producto));
  }
}