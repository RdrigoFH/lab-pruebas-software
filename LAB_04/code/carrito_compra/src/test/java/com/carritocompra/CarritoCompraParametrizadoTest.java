package com.carritocompra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Pruebas parametrizadas de {@link CarritoCompra} con distintos montos, descuentos e impuestos.
 *
 * <p>Verifica que la fórmula {@code total = (subtotal - descuento) + impuesto} sea correcta para
 * una amplia variedad de combinaciones de valores.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CarritoCompra — pruebas parametrizadas")
class CarritoCompraParametrizadoTest {

  @Mock private ServicioPrecio servicioPrecioMock;

  private CarritoCompra carrito;

  @BeforeEach
  void setUp() {
    carrito = new CarritoCompra(servicioPrecioMock);
  }

  /**
   * Verifica el cálculo del total para distintas combinaciones de precio, cantidad, descuento e
   * impuesto.
   *
   * <p>Parámetros (en orden): precio unitario, cantidad, descuento, impuesto, total esperado.
   */
  @ParameterizedTest(name = "precio={0}, cantidad={1}, descuento={2}, impuesto={3} → total={4}")
  @CsvSource({
    // precio,  cantidad, descuento, impuesto, totalEsperado
    "100.0,  1,  0.0,   0.0,   100.0", // sin cargos
    "100.0,  1,  10.0,  18.0,  108.0", // descuento + IGV típico
    "100.0,  1,  100.0, 0.0,   0.0", // descuento total
    "500.0,  2,  50.0,  180.0, 1130.0", // monto alto
    "0.01,   1,  0.0,   0.0,   0.01", // precio mínimo
    "1000.0, 1,  0.0,   180.0, 1180.0", // solo IGV 18%
    "200.0,  3,  30.0,  90.0,  660.0", // combinación media
    "50.0,   10, 25.0,  0.0,   475.0", // solo descuento
    "100.0,  5,  0.0,   90.0,  590.0", // solo impuesto
    "1.0,    100, 5.0,  14.0,  109.0" // muchas unidades, precio bajo
  })
  @DisplayName("total calculado correctamente para distintos montos")
  void totalCalculadoCorrectamente(
      double precio, int cantidad, double descuento, double impuesto, double totalEsperado) {

    Producto producto = new Producto("PTEST", "Producto Test", precio, true);
    carrito.agregarProducto(producto, cantidad);

    double subtotal = precio * cantidad;
    when(servicioPrecioMock.calcularDescuento(subtotal)).thenReturn(descuento);
    when(servicioPrecioMock.calcularImpuesto(subtotal)).thenReturn(impuesto);

    assertEquals(totalEsperado, carrito.calcularTotal(), 0.001);
  }

  /**
   * Verifica que el subtotal sea correcto para distintas combinaciones de precio y cantidad, sin
   * intervención del servicio de precios.
   */
  @ParameterizedTest(name = "precio={0} x cantidad={1} = subtotal={2}")
  @CsvSource({
    "10.0,  1,   10.0",
    "10.0,  5,   50.0",
    "33.3,  3,   99.9",
    "0.01,  100, 1.0",
    "999.9, 2,   1999.8"
  })
  @DisplayName("subtotal correcto para distintas combinaciones de precio y cantidad")
  void subtotalCorrecto(double precio, int cantidad, double subtotalEsperado) {
    Producto producto = new Producto("PTEST", "Producto Test", precio, true);
    carrito.agregarProducto(producto, cantidad);

    assertEquals(subtotalEsperado, carrito.calcularSubtotal(), 0.001);
  }
}

