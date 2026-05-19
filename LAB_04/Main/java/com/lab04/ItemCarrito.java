package com.lab04;

import java.time.LocalDateTime;

/**
 * Representa un ítem dentro del carrito de compras.
 */
public class ItemCarrito {
  private final Producto producto;
  private int cantidad;
  private final LocalDateTime fechaAgregado;

  public ItemCarrito(Producto producto, int cantidad) {
    if (producto == null) {
      throw new IllegalArgumentException("El producto no puede ser nulo");
    }
    if (cantidad <= 0) {
      throw new IllegalArgumentException("La cantidad debe ser positiva");
    }
    if (!producto.isDisponible()) {
      throw new IllegalStateException("El producto no está disponible");
    }
    this.producto = producto;
    this.cantidad = cantidad;
    this.fechaAgregado = LocalDateTime.now();
  }

  public Producto getProducto() {
    return producto;
  }

  public int getCantidad() {
    return cantidad;
  }

  public LocalDateTime getFechaAgregado() {
    return fechaAgregado;
  }

  public void setCantidad(int cantidad) {
    if (cantidad <= 0) {
      throw new IllegalArgumentException("La cantidad debe ser positiva");
    }
    this.cantidad = cantidad;
  }

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