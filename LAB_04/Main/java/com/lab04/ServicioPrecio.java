package com.lab04;

/**
 * Interfaz para servicios externos de cálculo de precios.
 */
public interface ServicioPrecio {
  double calcularDescuento(double monto);
  double calcularImpuesto(double monto);
}