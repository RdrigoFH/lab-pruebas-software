package com.gestorinventario;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Integración: Producto y Registro de Movimientos")
class ProductoMovimientoIntegracionTest {

  private Producto producto;

  @BeforeEach
  void prepararEscenario() {
    // Inicializamos con stock "0" para aislar completamente los movimientos
    // generados durante la ejecución de cada prueba.
    producto = new Producto("INT-001", "Producto de Integración", "100.00", "0");
  }

  @Nested
  @DisplayName("1. Flujo exitoso de registro de movimiento")
  class FlujoRegistroExitoso {

    @Test
    @DisplayName("Operaciones válidas de stock generan registros en el historial")
    void dadoOperacionesExitosas_cuandoSeModificaStock_entoncesSeRegistranMovimientos() {
      // Act
      producto.agregarStock(20);
      producto.extraerStock(5);

      // Assert
      List<Movimiento> historial = producto.obtenerHistorial();

      assertNotNull(historial, "El historial de movimientos no debe ser nulo");
      assertEquals(2, historial.size(), "Deberían haberse registrado exactamente 2 movimientos");

      // Verificamos la integridad del primer movimiento (Entrada)
      Movimiento primerMovimiento = historial.get(0);
      assertAll(
          "El primer registro debe ser una entrada de 20 unidades",
          () -> assertEquals(TipoMovimiento.ENTRADA, primerMovimiento.getTipo()),
          () -> assertEquals(20, primerMovimiento.getCantidad()),
          () -> assertNotNull(primerMovimiento.getFecha()));

      // Verificamos la integridad del segundo movimiento (Salida)
      Movimiento segundoMovimiento = historial.get(1);
      assertAll(
          "El segundo registro debe ser una salida de 5 unidades",
          () -> assertEquals(TipoMovimiento.SALIDA, segundoMovimiento.getTipo()),
          () -> assertEquals(5, segundoMovimiento.getCantidad()),
          () -> assertNotNull(segundoMovimiento.getFecha()));
    }
  }

  @Nested
  @DisplayName("2. Consistencia y transaccionalidad ante fallos")
  class ConsistenciaAnteFallos {

    @Test
    @DisplayName("Operaciones fallidas no dejan registros fantasma")
    void dadoOperacionInvalida_cuandoFalla_entoncesHistorialSeMantieneIntacto() {
      // Arrange
      producto.agregarStock(10);

      // Act: Intentamos una extracción ilegal (15 > 10)
      assertThrows(
          IllegalArgumentException.class,
          () -> producto.extraerStock(15),
          "La extracción debe fallar por falta de stock");

      // Assert
      List<Movimiento> historial = producto.obtenerHistorial();

      assertAll(
          "El historial debe reflejar únicamente las operaciones que fueron exitosas",
          () ->
              assertEquals(
                  1, historial.size(), "Solo debe existir el registro de la entrada inicial"),
          () -> assertEquals(TipoMovimiento.ENTRADA, historial.get(0).getTipo()));
    }
  }
}
