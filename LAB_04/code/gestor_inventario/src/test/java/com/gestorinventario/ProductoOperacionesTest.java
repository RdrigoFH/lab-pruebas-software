package com.gestorinventario;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Validación de operaciones y comportamiento de negocio en {@link Producto}.
 *
 * <p>Esta suite asume que la construcción del objeto es correcta (verificada en {@code
 * ProductoconstructorTest}). Se enfoca en las transacciones de inventario: agregar stock, extraer
 * stock, impedir inconsistencias (como stock negativo) y verificar cálculos derivados como el valor
 * total del inventario.
 */
@DisplayName("Producto Operaciones de Stock y Cálculos")
class ProductoOperacionesTest {

  // Constantes base para garantizar un estado inicial predecible
  private static final String CODIGO_BASE = "SKU-999";
  private static final String NOMBRE_BASE = "Teclado Mecánico";
  private static final String PRECIO_BASE_STR = "50.00";
  private static final String STOCK_INICIAL_STR = "20";

  private static final double PRECIO_BASE_NUM = 50.00;
  private static final int STOCK_INICIAL_NUM = 20;

  /** Producto bajo prueba, reiniciado a un estado conocido antes de cada test. */
  private Producto producto;

  @BeforeEach
  void prepararContexto() {
    producto = new Producto(CODIGO_BASE, NOMBRE_BASE, PRECIO_BASE_STR, STOCK_INICIAL_STR);
  }

  // OPERACIÓN: AGREGAR STOCK

  @Nested
  @DisplayName("1. Operación: agregarStock(int)")
  class AgregarStock {

    @ParameterizedTest(name = "Al agregar {0} unidades, el stock suma {1}")
    @MethodSource("proveerCantidadesParaAgregar")
    @DisplayName("Agregar cantidades válidas incrementa el stock correctamente")
    void dadoCantidadValida_cuandoAgregarStock_entoncesStockSeIncrementa(
        int cantidadAgregada, int stockEsperado) {
      // Act
      producto.agregarStock(cantidadAgregada);

      // Assert
      assertEquals(
          stockEsperado,
          producto.consultarStock(),
          "El stock debe ser la suma del inicial más lo agregado");
    }

    static Stream<Arguments> proveerCantidadesParaAgregar() {
      return Stream.of(
          Arguments.of(1, STOCK_INICIAL_NUM + 1), // Incremento mínimo
          Arguments.of(15, STOCK_INICIAL_NUM + 15), // Incremento estándar
          Arguments.of(1000, STOCK_INICIAL_NUM + 1000) // Incremento masivo
          );
    }

    @Test
    @DisplayName("Agregar cero unidades no altera el stock (Caso límite)")
    void dadoCantidadCero_cuandoAgregarStock_entoncesStockSeMantiene() {
      // Act
      producto.agregarStock(0);

      // Assert
      assertEquals(STOCK_INICIAL_NUM, producto.consultarStock());
    }

    @ParameterizedTest(name = "Rechazo de cantidad {0}")
    @ValueSource(ints = {-1, -10, Integer.MIN_VALUE})
    @DisplayName("Agregar cantidades negativas lanza IllegalArgumentException")
    void dadoCantidadNegativa_cuandoAgregarStock_entoncesLanzaExcepcion(int cantidadNegativa) {
      // Act & Assert
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> producto.agregarStock(cantidadNegativa),
              "El sistema debe bloquear intentos de corromper el stock con sumas negativas");

      assertMensajeContiene(ex, "negativa");
    }
  }

  // OPERACIÓN: EXTRAER STOCK

  @Nested
  @DisplayName("2. Operación: extraerStock(int)")
  class ExtraerStock {

    @ParameterizedTest(name = "Al extraer {0} unidades, quedan {1}")
    @MethodSource("proveerCantidadesParaExtraer")
    @DisplayName("Extraer cantidades válidas decrementa el stock correctamente")
    void dadoCantidadValida_cuandoExtraerStock_entoncesStockSeReduce(
        int cantidadExtraida, int stockEsperado) {
      // Act
      producto.extraerStock(cantidadExtraida);

      // Assert
      assertEquals(
          stockEsperado,
          producto.consultarStock(),
          "El stock final debe ser la resta del inicial menos lo extraído");
    }

    static Stream<Arguments> proveerCantidadesParaExtraer() {
      return Stream.of(
          Arguments.of(1, STOCK_INICIAL_NUM - 1), // Extracción mínima
          Arguments.of(10, STOCK_INICIAL_NUM - 10), // Extracción parcial
          Arguments.of(STOCK_INICIAL_NUM, 0) // Vaciado total del stock (límite)
          );
    }

    @Test
    @DisplayName(
        "Extraer más unidades de las disponibles lanza IllegalArgumentException (Sobregiro)")
    void dadoCantidadMayorAlStock_cuandoExtraerStock_entoncesLanzaExcepcion() {
      // Arrange
      int cantidadExcesiva = STOCK_INICIAL_NUM + 1; // Unidades excedentes

      // Act & Assert
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> producto.extraerStock(cantidadExcesiva),
              "No se debe permitir dejar el stock en valores negativos");

      assertMensajeContiene(ex, "negativo");
      // Verifica que la transacción fallida no alteró el estado original (atomicidad)
      assertEquals(STOCK_INICIAL_NUM, producto.consultarStock(), "El stock no debió ser alterado");
    }

    @Test
    @DisplayName("Extraer cero unidades no altera el stock (Caso límite)")
    void dadoCantidadCero_cuandoExtraerStock_entoncesStockSeMantiene() {
      // Act
      producto.extraerStock(0);

      // Assert
      assertEquals(STOCK_INICIAL_NUM, producto.consultarStock());
    }

    @ParameterizedTest(name = "Rechazo de extracción {0}")
    @ValueSource(ints = {-1, -50})
    @DisplayName("Extraer cantidades negativas lanza IllegalArgumentException")
    void dadoCantidadNegativa_cuandoExtraerStock_entoncesLanzaExcepcion(int cantidadNegativa) {
      // Act & Assert
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> producto.extraerStock(cantidadNegativa),
              "No se debe permitir usar valores negativos que actúen matemáticamente como sumas");

      assertMensajeContiene(ex, "negativa");
    }
  }

  // CÁLCULOS DERIVADOS

  @Nested
  @DisplayName("3. Cálculo de valor del stock total")
  class CalculoValorTotal {

    @Test
    @DisplayName("Calcula correctamente el valor total del inventario actual")
    void dadoStockYPrecio_cuandoObtenerValorTotal_entoncesCalculaCorrectamente() {
      // Arrange
      double valorEsperado = STOCK_INICIAL_NUM * PRECIO_BASE_NUM; // 20 * 50.00 = 1000.00

      // Act
      double valorTotal = producto.obtenerValorTotal();

      // Assert
      assertEquals(valorEsperado, valorTotal, 0.001, "El valor total debe ser precio * cantidad");
    }

    @Test
    @DisplayName("El valor total se actualiza dinámicamente tras alteraciones de stock")
    void dadoMovimientosDeStock_cuandoObtenerValorTotal_entoncesReflejaCambios() {
      // Arrange
      producto.agregarStock(10); // Stock = 30
      producto.extraerStock(5); // Stock = 25
      double valorEsperadoFinal = 25 * PRECIO_BASE_NUM;

      // Act
      double valorTotal = producto.obtenerValorTotal();

      // Assert
      assertEquals(valorEsperadoFinal, valorTotal, 0.001);
    }

    @Test
    @DisplayName("El valor total de un producto sin stock es 0.0")
    void dadoStockVaciado_cuandoObtenerValorTotal_entoncesRetornaCero() {
      // Arrange
      producto.extraerStock(STOCK_INICIAL_NUM); // Stock pasa a ser 0

      // Act
      double valorTotal = producto.obtenerValorTotal();

      // Assert
      assertEquals(0.0, valorTotal, 0.001);
    }
  }

  // MÉTODOS DE VALIDACIÓN INTERNA (Estado de invariantes)

  @Nested
  @DisplayName("4. Métodos de estado: codigoValido(), precioValido(), cantidadValida()")
  class EstadoValidacionContinua {

    /**
     * Dado que el constructor garantiza el estado válido inicial y los métodos de operación están
     * blindados, estos validadores booleanos deben retornar true de manera consistente. Sirve para
     * documentar la garantía de invariabilidad del objeto en memoria.
     */
    @Test
    @DisplayName("Un producto exitosamente construido reporta estado válido en todos sus campos")
    void dadoProductoConstruido_entoncesValidadoresInternosSonVerdaderos() {
      assertAll(
          "Las validaciones internas siempre deben certificar la salud del objeto",
          () -> assertTrue(producto.codigoValido(), "El código debería ser reportado como válido"),
          () -> assertTrue(producto.precioValido(), "El precio debería ser reportado como válido"),
          () ->
              assertTrue(
                  producto.cantidadValida(), "La cantidad debería ser reportada como válida"));
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
