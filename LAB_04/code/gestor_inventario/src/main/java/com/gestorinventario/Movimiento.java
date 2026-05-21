package com.gestorinventario;

import java.time.LocalDateTime;

public class Movimiento {

  private TipoMovimiento tipo;
  private int cantidad;
  private LocalDateTime fecha;

  public Movimiento(TipoMovimiento tipo, int cantidad) {
    this(tipo, cantidad, LocalDateTime.now());
  }

  public Movimiento(TipoMovimiento tipo, int cantidad, LocalDateTime fecha) {
    validarTipo(tipo);
    validarCantidad(cantidad);
    validarFecha(fecha);

    this.tipo = tipo;
    this.cantidad = cantidad;
    this.fecha = fecha;
  }

  public TipoMovimiento getTipo() {
    return tipo;
  }

  public int getCantidad() {
    return cantidad;
  }

  public LocalDateTime getFecha() {
    return fecha;
  }

  private static void validarTipo(TipoMovimiento tipo) {
    if (tipo == null) {
      throw new IllegalArgumentException("El tipo no puede ser nulo");
    }
  }

  private static void validarCantidad(int cantidad) {
    if (cantidad <= 0) {
      throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
    }
  }

  private static void validarFecha(LocalDateTime fecha) {
    if (fecha == null) {
      throw new IllegalArgumentException("La fecha no puede ser nula");
    }

    if (fecha.isAfter(LocalDateTime.now())) {
      throw new IllegalArgumentException("La fecha no puede ser futura");
    }
  }
}
