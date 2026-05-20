package com.carritocompra;

import java.util.Objects;

/**
 * Representa un producto disponible en la tienda en línea.
 *
 * <p>La igualdad entre productos se determina únicamente por su {@code id}, por lo que dos
 * instancias con el mismo identificador se consideran el mismo producto.
 */
public class Producto {

  private final String id;
  private final String nombre;
  private final double precio;
  private boolean disponible;

  /**
   * Construye un nuevo producto.
   *
   * @param id identificador único, no nulo ni vacío
   * @param nombre nombre descriptivo, no nulo ni vacío
   * @param precio precio unitario, debe ser positivo
   * @param disponible {@code true} si el producto tiene stock
   * @throws IllegalArgumentException si id, nombre o precio son inválidos
   */
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

  /**
   * @return identificador único del producto
   */
  public String getId() {
    return id;
  }

  /**
   * @return nombre descriptivo del producto
   */
  public String getNombre() {
    return nombre;
  }

  /**
   * @return precio unitario del producto
   */
  public double getPrecio() {
    return precio;
  }

  /**
   * @return {@code true} si el producto está disponible para agregar al carrito
   */
  public boolean isDisponible() {
    return disponible;
  }

  /**
   * Actualiza la disponibilidad del producto.
   *
   * @param disponible nuevo estado de disponibilidad
   */
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

