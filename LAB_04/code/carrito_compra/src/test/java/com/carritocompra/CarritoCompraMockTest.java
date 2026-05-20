package com.carritocompra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Pruebas de {@link CarritoCompra} con {@link ServicioPrecio} simulado mediante Mockito.
 *
 * <p>Verifica que el carrito delegue correctamente los cálculos de descuento e impuesto, y que el
 * total final sea matemáticamente coherente con los valores del mock.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CarritoCompra — pruebas con Mockito")
class CarritoCompraMockTest {

  @Mock private ServicioPrecio servicioPrecioMock;

  private Producto laptop;
  private Producto mouse;
  private CarritoCompra carrito;

  @BeforeEach
  void setUp() {
    laptop = new Producto("P001", "Laptop", 1000.0, true);
    mouse = new Producto("P002", "Mouse", 25.0, true);
    carrito = new CarritoCompra(servicioPrecioMock);
  }

  // -------------------------------------------------------------------------
  // Cálculo de descuento
  // -------------------------------------------------------------------------

  @Nested
  @DisplayName("Cálculo de descuento con mock")
  class CalculoDescuento {

    @Test
    @DisplayName("calcularDescuento delega al servicio con el subtotal correcto")
    void calcularDescuentoDelegaAlServicio() {
      carrito.agregarProducto(laptop, 1);
      when(servicioPrecioMock.calcularDescuento(1000.0)).thenReturn(100.0);

      double descuento = carrito.calcularDescuento();

      assertEquals(100.0, descuento);
      verify(servicioPrecioMock).calcularDescuento(1000.0);
    }

    @Test
    @DisplayName("descuento es cero cuando el servicio retorna cero")
    void descuentoCeroDelServicio() {
      carrito.agregarProducto(laptop, 1);
      when(servicioPrecioMock.calcularDescuento(1000.0)).thenReturn(0.0);

      assertEquals(0.0, carrito.calcularDescuento());
    }
  }

  // -------------------------------------------------------------------------
  // Cálculo de impuesto
  // -------------------------------------------------------------------------

  @Nested
  @DisplayName("Cálculo de impuesto con mock")
  class CalculoImpuesto {

    @Test
    @DisplayName("calcularImpuesto delega al servicio con el subtotal correcto")
    void calcularImpuestoDelegaAlServicio() {
      carrito.agregarProducto(laptop, 1);
      when(servicioPrecioMock.calcularImpuesto(1000.0)).thenReturn(180.0);

      double impuesto = carrito.calcularImpuesto();

      assertEquals(180.0, impuesto);
      verify(servicioPrecioMock).calcularImpuesto(1000.0);
    }
  }

  // -------------------------------------------------------------------------
  // Cálculo de total
  // -------------------------------------------------------------------------

  @Nested
  @DisplayName("Cálculo de total con mock")
  class CalculoTotal {

    @Test
    @DisplayName("total = subtotal - descuento + impuesto")
    void totalConDescuentoEImpuesto() {
      carrito.agregarProducto(laptop, 1); // subtotal = 1000
      when(servicioPrecioMock.calcularDescuento(1000.0)).thenReturn(100.0);
      when(servicioPrecioMock.calcularImpuesto(1000.0)).thenReturn(180.0);

      double total = carrito.calcularTotal();

      assertEquals(1080.0, total, 0.001);
    }

    @Test
    @DisplayName("total es cero para carrito vacío (sin llamadas al servicio)")
    void totalCarritoVacio() {
      when(servicioPrecioMock.calcularDescuento(0.0)).thenReturn(0.0);
      when(servicioPrecioMock.calcularImpuesto(0.0)).thenReturn(0.0);

      assertEquals(0.0, carrito.calcularTotal());
    }

    @Test
    @DisplayName("total con solo impuesto (sin descuento)")
    void totalSoloImpuesto() {
      carrito.agregarProducto(mouse, 4); // subtotal = 100
      when(servicioPrecioMock.calcularDescuento(100.0)).thenReturn(0.0);
      when(servicioPrecioMock.calcularImpuesto(100.0)).thenReturn(18.0);

      assertEquals(118.0, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("total con solo descuento (sin impuesto)")
    void totalSoloDescuento() {
      carrito.agregarProducto(mouse, 4); // subtotal = 100
      when(servicioPrecioMock.calcularDescuento(100.0)).thenReturn(10.0);
      when(servicioPrecioMock.calcularImpuesto(100.0)).thenReturn(0.0);

      assertEquals(90.0, carrito.calcularTotal(), 0.001);
    }
  }

  // -------------------------------------------------------------------------
  // Verificación de llamadas al servicio
  // -------------------------------------------------------------------------

  @Nested
  @DisplayName("Verificación de llamadas a ServicioPrecio")
  class VerificacionLlamadas {

    @Test
    @DisplayName("calcularTotal invoca calcularDescuento exactamente una vez")
    void calcularTotalInvocaDescuentoUnaVez() {
      carrito.agregarProducto(laptop, 1);
      when(servicioPrecioMock.calcularDescuento(anyDouble())).thenReturn(0.0);
      when(servicioPrecioMock.calcularImpuesto(anyDouble())).thenReturn(0.0);

      carrito.calcularTotal();

      verify(servicioPrecioMock, times(1)).calcularDescuento(1000.0);
    }

    @Test
    @DisplayName("calcularTotal invoca calcularImpuesto exactamente una vez")
    void calcularTotalInvocaImpuestoUnaVez() {
      carrito.agregarProducto(laptop, 1);
      when(servicioPrecioMock.calcularDescuento(anyDouble())).thenReturn(0.0);
      when(servicioPrecioMock.calcularImpuesto(anyDouble())).thenReturn(0.0);

      carrito.calcularTotal();

      verify(servicioPrecioMock, times(1)).calcularImpuesto(1000.0);
    }

    @Test
    @DisplayName("obtenerResumenCompra invoca el servicio al menos una vez por concepto")
    void resumenInvocaServicioAlMenosUnaVez() {
      carrito.agregarProducto(laptop, 1);
      when(servicioPrecioMock.calcularDescuento(anyDouble())).thenReturn(0.0);
      when(servicioPrecioMock.calcularImpuesto(anyDouble())).thenReturn(0.0);

      carrito.obtenerResumenCompra();

      verify(servicioPrecioMock, atLeastOnce()).calcularDescuento(anyDouble());
      verify(servicioPrecioMock, atLeastOnce()).calcularImpuesto(anyDouble());
    }

    @Test
    @DisplayName("servicio nunca es invocado antes de agregar productos al calcular total")
    void servicioNoInvocadoAntesDeAgregarProductos() {
      // No se agrega ningún producto; el subtotal es 0
      when(servicioPrecioMock.calcularDescuento(0.0)).thenReturn(0.0);
      when(servicioPrecioMock.calcularImpuesto(0.0)).thenReturn(0.0);

      carrito.calcularTotal();

      verify(servicioPrecioMock, never()).calcularDescuento(1000.0);
      verify(servicioPrecioMock, never()).calcularImpuesto(1000.0);
    }

    @Test
    @DisplayName("resumen de carrito vacío no invoca al servicio")
    void resumenCarritoVacioNoInvocaServicio() {
      carrito.obtenerResumenCompra();

      verify(servicioPrecioMock, never()).calcularDescuento(anyDouble());
      verify(servicioPrecioMock, never()).calcularImpuesto(anyDouble());
    }

    @Test
    @DisplayName("historial contiene entrada del total calculado")
    void historialContieneEntradaDeTotal() {
      carrito.agregarProducto(laptop, 1);
      when(servicioPrecioMock.calcularDescuento(anyDouble())).thenReturn(50.0);
      when(servicioPrecioMock.calcularImpuesto(anyDouble())).thenReturn(170.0);

      carrito.calcularTotal();

      boolean hayEntradaTotal =
          carrito.getHistorialOperaciones().stream().anyMatch(op -> op.contains("Total calculado"));
      assertTrue(hayEntradaTotal);
    }
  }
}

