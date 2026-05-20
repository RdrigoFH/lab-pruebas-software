package com.lab04;

import java.time.LocalDateTime;

/**
 * Representa un ítem dentro del carrito de compras.
 *
 * <p>Agrupa un {@link Producto} con su cantidad y registra el momento en que fue agregado.
 * La validación de disponibilidad del producto es responsabilidad de {@link CarritoCompra};
 * este clase solo garantiza que los valores propios (cantidad) sean válidos.
 */
public class ItemCarrito {

  private final Producto producto;
  private int cantidad;
  private final LocalDateTime fechaAgregado;

  /**
   * Construye un ítem de carrito.
   *
   * @param producto producto asociado, no puede ser nulo
   * @param cantidad cantidad inicial, debe ser positiva
   * @throws IllegalArgumentException si el producto es nulo o la cantidad no es positiva
   */
  public ItemCarrito(Producto producto, int cantidad) {
    if (producto == null) {
      throw new IllegalArgumentException("El producto no puede ser nulo");
    }
    if (cantidad <= 0) {
      throw new IllegalArgumentException("La cantidad debe ser positiva");
    }
    this.producto = producto;
    this.cantidad = cantidad;
    this.fechaAgregado = LocalDateTime.now();
  }

  /** @return el producto asociado a este ítem */
  public Producto getProducto() {
    return producto;
  }

  /** @return la cantidad actual de este ítem */
  public int getCantidad() {
    return cantidad;
  }

  /** @return la fecha y hora en que se agregó este ítem al carrito */
  public LocalDateTime getFechaAgregado() {
    return fechaAgregado;
  }

  /**
   * Actualiza la cantidad del ítem.
   *
   * @param cantidad nueva cantidad, debe ser positiva
   * @throws IllegalArgumentException si la cantidad no es positiva
   */
  public void setCantidad(int cantidad) {
    if (cantidad <= 0) {
      throw new IllegalArgumentException("La cantidad debe ser positiva");
    }
    this.cantidad = cantidad;
  }

  /** @return el precio total de este ítem (precio unitario × cantidad) */
  public double getSubtotal() {
    return producto.getPrecio() * cantidad;
  }

  @Override
  public String toString() {
    return String.format(
        "ItemCarrito{producto=%s, cantidad=%d, subtotal=%.2f}",
        producto.getNombre(), cantidad, getSubtotal());
  }
}