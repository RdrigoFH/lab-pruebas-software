package com.lab04;

/**
 * Interfaz para servicios externos de cálculo de precios.
 *
 * <p>Define los contratos para aplicar descuentos e impuestos sobre un monto base.
 * Las implementaciones concretas (o mocks en tests) deben garantizar que los valores
 * retornados sean no negativos.
 */
public interface ServicioPrecio {

  /**
   * Calcula el descuento a aplicar sobre el monto dado.
   *
   * @param monto monto base sobre el que se calcula el descuento
   * @return valor del descuento (no negativo)
   */
  double calcularDescuento(double monto);

  /**
   * Calcula el impuesto a aplicar sobre el monto dado.
   *
   * @param monto monto base sobre el que se calcula el impuesto
   * @return valor del impuesto (no negativo)
   */
  double calcularImpuesto(double monto);
}