package com.gestorinventario;

import java.time.LocalDateTime;

public class Movimiento {

  private TipoMovimiento tipo;
  private int cantidad;
  private LocalDateTime fecha;

  public Movimiento(TipoMovimiento tipo, int cantidad) {}

  public Movimiento(TipoMovimiento tipo, int cantidad, LocalDateTime fecha) {}

  public TipoMovimiento getTipo() {
    return tipo;
  }

  public int getCantidad() {
    return cantidad;
  }

  public LocalDateTime getFecha() {
    return fecha;
  }
}
