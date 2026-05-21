package com.carritocompra;

public class Producto {

  private String id;
  private String nombre;
  private double precio;
  private boolean disponibilidad;

  public Producto(String id, String nombre, double precio, boolean disponibilidad) {
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
    this.disponibilidad = disponibilidad;
  }

  public String getId() {
    return id;
  }

  public double getPrecio() {
    return precio;
  }

  public boolean getDisponibilidad() {
    return disponibilidad;
  }

  public String getNombre() {
    return nombre;
  }
}

