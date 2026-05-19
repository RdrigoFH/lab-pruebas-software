package com.lab04;

import java.util.Objects;

/**
 * Representa un producto disponible en la tienda.
 */
public class Producto {
  private final String id;
  private final String nombre;
  private final double precio;
  private boolean disponible;

  public Producto(String id, String nombre, double precio, boolean disponible) {
    if (id == null || id.trim().isEmpty()) {
      throw new IllegalArgumentException("El ID del producto no puede estar vacío");
    }
    if (nombre == null || nombre.trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
    }
    if (precio <= 0) {
      throw new IllegalArgumentException("El precio debe ser positivo");
    }
    this.id = id;
    this.nombre = nombre;
    this.precio = precio;
    this.disponible = disponible;
  }

  public String getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public double getPrecio() {
    return precio;
  }

  public boolean isDisponible() {
    return disponible;
  }

  public void setDisponible(boolean disponible) {
    this.disponible = disponible;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Producto producto = (Producto) o;
    return Objects.equals(id, producto.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return String.format(
        "Producto{id='%s', nombre='%s', precio=%.2f, disponible=%s}",
        id, nombre, precio, disponible);
  }
}