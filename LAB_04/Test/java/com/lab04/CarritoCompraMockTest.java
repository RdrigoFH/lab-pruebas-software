package com.lab04;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("CarritoCompra - Pruebas Avanzadas con Mockito")
class CarritoCompraMockTest {

  @Mock
  private ServicioPrecio servicioPrecio;

  @Captor
  private ArgumentCaptor<Double> montoCaptor;

  private CarritoCompra carrito;
  private Producto laptop;
  private Producto mouse;
  private Producto monitor;

  @BeforeEach
  void setUp() {
    carrito = new CarritoCompra(servicioPrecio);
    laptop = new Producto("P001", "Laptop", 1500.00, true);
    mouse = new Producto("P002", "Mouse", 25.00, true);
    monitor = new Producto("P003", "Monitor", 300.00, true);
  }

  @Nested
  @DisplayName("Verificación de llamadas a ServicioPrecio")
  class VerificacionLlamadasTests {

    @Test
    @DisplayName("calcularDescuento debe ser llamado con el subtotal correcto")
    void calcularDescuentoDebeSerLlamadoConSubtotalCorrecto() {
      carrito.agregarProducto(laptop, 2);
      carrito.agregarProducto(mouse, 4);

      carrito.calcularDescuento();

      verify(servicioPrecio).calcularDescuento(3100.0);
    }

    @Test
    @DisplayName("calcularImpuesto debe ser llamado con el subtotal correcto")
    void calcularImpuestoDebeSerLlamadoConSubtotalCorrecto() {
      carrito.agregarProducto(laptop, 1);

      carrito.calcularImpuesto();

      verify(servicioPrecio).calcularImpuesto(1500.0);
    }

    @Test
    @DisplayName("calcularTotal debe llamar a ambos métodos")
    void calcularTotalDebeLlamarAmbosMetodos() {
      carrito.agregarProducto(laptop, 1);

      carrito.calcularTotal();

      verify(servicioPrecio, atLeastOnce()).calcularDescuento(anyDouble());
      verify(servicioPrecio, atLeastOnce()).calcularImpuesto(anyDouble());
    }

    @Test
    @DisplayName("Verificar que el monto pasado a los servicios es correcto")
    void verificarMontoPasadoServicios() {
      carrito.agregarProducto(laptop, 3);
      carrito.agregarProducto(monitor, 2);

      carrito.calcularTotal();

      verify(servicioPrecio, atLeastOnce()).calcularDescuento(montoCaptor.capture());
      assertEquals(5100.0, montoCaptor.getValue());
    }
  }

  @Nested
  @DisplayName("Simulación de diferentes comportamientos del servicio")
  class SimulacionServiciosTests {

    @Test
    @DisplayName("Con 0% descuento y 0% impuesto")
    void conCeroPorcientoDescuentoYCeroPorcientoImpuesto() {
      when(servicioPrecio.calcularDescuento(anyDouble())).thenReturn(0.0);
      when(servicioPrecio.calcularImpuesto(anyDouble())).thenReturn(0.0);

      carrito.agregarProducto(laptop, 2);

      assertEquals(3000.0, carrito.calcularTotal());
    }

    @Test
    @DisplayName("Con 10% descuento y 19% impuesto")
    void conDiezPorcientoDescuentoYDiecinuevePorcientoImpuesto() {
      when(servicioPrecio.calcularDescuento(anyDouble()))
          .thenAnswer(invocation -> (double) invocation.getArgument(0) * 0.10);
      when(servicioPrecio.calcularImpuesto(anyDouble()))
          .thenAnswer(invocation -> (double) invocation.getArgument(0) * 0.19);

      carrito.agregarProducto(laptop, 1);

      assertEquals(1635.0, carrito.calcularTotal(), 0.01);
    }

    @Test
    @DisplayName("Servicio lanza excepción - debe propagarse")
    void servicioLanzaExcepcionDebePropagarse() {
      when(servicioPrecio.calcularDescuento(anyDouble()))
          .thenThrow(new RuntimeException("Error en servicio externo"));

      carrito.agregarProducto(laptop, 1);

      assertThrows(RuntimeException.class, () -> carrito.calcularTotal());
    }

    @Test
    @DisplayName("Verificar número de llamadas a cada método")
    void verificarNumeroDeLlamadas() {
      carrito.agregarProducto(laptop, 1);

      carrito.calcularTotal();
      carrito.calcularTotal();

      verify(servicioPrecio, times(2)).calcularDescuento(anyDouble());
      verify(servicioPrecio, times(2)).calcularImpuesto(anyDouble());
    }
  }

  @Nested
  @DisplayName("Pruebas de integración con mocks")
  class IntegracionConMocksTests {

    @Test
    @DisplayName("Escenario completo de compra")
    void escenarioCompletoDeCompra() {
      when(servicioPrecio.calcularDescuento(anyDouble()))
          .thenAnswer(inv -> (double) inv.getArgument(0) * 0.05);
      when(servicioPrecio.calcularImpuesto(anyDouble()))
          .thenAnswer(inv -> (double) inv.getArgument(0) * 0.19);

      carrito.agregarProducto(laptop, 1);
      carrito.agregarProducto(mouse, 3);
      carrito.agregarProducto(monitor, 2);

      double total = carrito.calcularTotal();

      assertEquals(2479.50, total, 0.01);

      String resumen = carrito.obtenerResumenCompra();
      assertTrue(resumen.contains("Laptop"));
      assertTrue(resumen.contains("Mouse"));
      assertTrue(resumen.contains("Monitor"));

      assertTrue(carrito.getHistorialOperaciones().size() >= 4);
    }
  }
}