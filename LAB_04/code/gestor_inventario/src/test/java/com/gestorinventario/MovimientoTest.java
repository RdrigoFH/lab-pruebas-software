package com.gestorinventario;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Construcción y validación de la entidad de trazabilidad {@link Movimiento}. */
@DisplayName("Movimiento: Construcción y validación de auditoría")
class MovimientoTest {

  // Constantes de prueba
  private static final int CANTIDAD_VALIDA = 15;

  // ESCENARIOS FELICES

  @Nested
  @DisplayName("1. Escenarios felices, registro de movimientos válido")
  class EscenariosFelices {

    @Test
    @DisplayName("Constructor asigna correctamente atributos con fecha explícita")
    void dadoDatosValidosYFecha_cuandoSeConstruye_entoncesAtributosSonAsignados() {
      // Arrange
      LocalDateTime fechaEsperada = LocalDateTime.of(2023, 10, 25, 14, 30);
      int cantidadEsperada = 50;

      // Act
      Movimiento movimiento =
          new Movimiento(TipoMovimiento.ENTRADA, cantidadEsperada, fechaEsperada);

      // Assert
      assertAll(
          "El movimiento debe reflejar exactamente los parámetros provistos",
          () -> assertEquals(TipoMovimiento.ENTRADA, movimiento.getTipo(), "tipo"),
          () -> assertEquals(cantidadEsperada, movimiento.getCantidad(), "cantidad"),
          () -> assertEquals(fechaEsperada, movimiento.getFecha(), "fecha"));
    }

    @Test
    @DisplayName("Constructor sin fecha genera automáticamente la fecha actual (LocalDateTime.now)")
    void dadoDatosValidosSinFecha_cuandoSeConstruye_entoncesGeneraFechaActual() {
      // Arrange
      // Tomamos un margen de tiempo justo antes de la creación
      LocalDateTime umbralInferior = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

      // Act
      Movimiento movimiento = new Movimiento(TipoMovimiento.SALIDA, CANTIDAD_VALIDA);

      // Assert
      LocalDateTime umbralSuperior =
          LocalDateTime.now().plusSeconds(1); // Margen de latencia del test

      assertAll(
          "El movimiento autogenerado debe ser válido y tener marca de tiempo reciente",
          () -> assertEquals(TipoMovimiento.SALIDA, movimiento.getTipo()),
          () -> assertEquals(CANTIDAD_VALIDA, movimiento.getCantidad()),
          () -> assertNotNull(movimiento.getFecha(), "La fecha autogenerada no puede ser nula"),
          () ->
              assertTrue(
                  !movimiento.getFecha().isBefore(umbralInferior)
                      && !movimiento.getFecha().isAfter(umbralSuperior),
                  "La fecha debe ser el instante exacto de la creación"));
    }
  }

  // VALIDACIÓN DE ATRIBUTOS: CANTIDAD

  @Nested
  @DisplayName("2. Validación de 'cantidad'")
  class ValidacionCantidad {

    static Stream<Arguments> cantidadesInvalidas() {
      return Stream.of(
          Arguments.of(0, "cero"),
          Arguments.of(-1, "negativo mínimo"),
          Arguments.of(-50, "negativo estándar"));
    }

    @ParameterizedTest(name = "cantidad {1} ({0}) → IllegalArgumentException")
    @MethodSource("cantidadesInvalidas")
    @DisplayName("Cantidades nulas o negativas lanzan IllegalArgumentException")
    void dadoCantidadInvalida_cuandoSeConstruye_entoncesLanzaExcepcion(
        int cantidadInvalida, String descripcion) {

      // Act & Assert
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> new Movimiento(TipoMovimiento.ENTRADA, cantidadInvalida),
              "Debe rechazar cantidad " + descripcion);

      assertMensajeContiene(ex, "cantidad");
    }
  }

  // VALIDACIÓN DE ATRIBUTOS: TIPO DE MOVIMIENTO

  @Nested
  @DisplayName("3. Validación de 'tipo'")
  class ValidacionTipo {

    @Test
    @DisplayName("Tipo de movimiento null lanza IllegalArgumentException")
    void dadoTipoNulo_cuandoSeConstruye_entoncesLanzaExcepcion() {
      // Arrange
      TipoMovimiento tipoNulo = null;

      // Act & Assert
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> new Movimiento(tipoNulo, CANTIDAD_VALIDA),
              "Un movimiento no puede existir sin una dirección (ENTRADA o SALIDA)");

      assertMensajeContiene(ex, "tipo");
    }
  }

  // VALIDACIÓN DE ATRIBUTOS: FECHA

  @Nested
  @DisplayName("4. Validación de 'fecha'")
  class ValidacionFecha {

    @Test
    @DisplayName("Fecha explícita null lanza IllegalArgumentException")
    void dadoFechaNula_cuandoSeConstruye_entoncesLanzaExcepcion() {
      // Arrange
      LocalDateTime fechaNula = null;

      // Act & Assert
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> new Movimiento(TipoMovimiento.ENTRADA, CANTIDAD_VALIDA, fechaNula),
              "Si se provee una fecha explícita, esta no puede ser null");

      assertMensajeContiene(ex, "fecha");
    }

    @Test
    @DisplayName(
        "Fechas futuras son rechazadas (No se puede registrar un movimiento que no ocurrió)")
    void dadoFechaFutura_cuandoSeConstruye_entoncesLanzaExcepcion() {
      // Arrange
      LocalDateTime fechaFutura = LocalDateTime.now().plusDays(1);

      // Act & Assert
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> new Movimiento(TipoMovimiento.SALIDA, CANTIDAD_VALIDA, fechaFutura),
              "El sistema de auditoría no debe aceptar predicciones del futuro");

      assertMensajeContiene(ex, "fecha");
    }
  }

  // MÉTODOS HELPER PRIVADOS

  /**
   * Verifica que el mensaje de la excepción proporcionada contiene la subcadena esperada.
   *
   * @param ex excepción cuyo mensaje se va a verificar
   * @param subcadenaEsperada texto que debe aparecer en el mensaje
   */
  private static void assertMensajeContiene(IllegalArgumentException ex, String subcadenaEsperada) {
    assertNotNull(ex.getMessage(), "El mensaje de la excepción no debe ser nulo");
    assertTrue(
        ex.getMessage().toLowerCase().contains(subcadenaEsperada.toLowerCase()),
        "El mensaje debe mencionar '"
            + subcadenaEsperada
            + "', pero fue: \""
            + ex.getMessage()
            + "\"");
  }
}
