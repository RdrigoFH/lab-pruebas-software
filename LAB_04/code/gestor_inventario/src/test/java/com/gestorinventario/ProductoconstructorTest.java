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
 * Construcción y validación de {@link Producto}.
 *
 * <p>Se enfoca exclusivamente en verificar que la clase {@code Producto} se construye correctamente
 * bajo entradas válidas y que rechaza cualquier dato inválido en sus atributos fundamentales:
 * {@code codigo}, {@code nombre}, {@code precio} y {@code cantidad}, procesándolos como cadenas de
 * texto según lo definido en su constructor.
 */
@DisplayName("Producto Construcción y validación de atributos")
class ProductoconstructorTest {

  // Constantes de prueba, valores canónicos reutilizados en toda la clase

  /** Código de producto válido usado como referencia en los tests felices. */
  private static final String CODIGO_VALIDO = "P001";

  /** Nombre de producto válido usado como referencia en los tests felices. */
  private static final String NOMBRE_VALIDO = "Cuaderno A4";

  /** Precio positivo válido en formato texto, usado como referencia en los tests felices. */
  private static final String PRECIO_VALIDO = "25.00";

  /** Cantidad inicial válida en formato texto (mayor que cero) usada en los tests felices. */
  private static final String CANTIDAD_VALIDA = "10";

  // Fixture

  /**
   * Producto de referencia reconstruido antes de cada prueba para garantizar aislamiento total
   * entre casos de prueba.
   */
  private Producto producto;

  /**
   * Inicializa el fixture compartido antes de cada método de prueba.
   *
   * <p>Se construye un {@link Producto} con datos completamente válidos para que los tests felices
   * puedan operar directamente sobre él sin necesidad de repetir el proceso de construcción.
   */
  @BeforeEach
  void inicializarProductoBase() {
    producto = new Producto(CODIGO_VALIDO, NOMBRE_VALIDO, PRECIO_VALIDO, CANTIDAD_VALIDA);
  }

  // ESCENARIOS FELICES

  /**
   * Pruebas que verifican el comportamiento correcto del constructor cuando todos los argumentos
   * cumplen las reglas de negocio y los formatos de parseo.
   *
   * <p>Comenzamos siempre con el <em>happy path</em> porque define el contrato mínimo que la
   * implementación debe cumplir. Si este grupo falla, el resto de los grupos carece de sentido.
   */
  @Nested
  @DisplayName("1. Escenarios felices, construcción válida")
  class EscenariosFelicess {

    /**
     * Verifica que el constructor parsea y asigna correctamente todos los atributos cuando los
     * datos de entrada (como texto) son válidos.
     */
    @Test
    @DisplayName("Constructor parsea y asigna todos los atributos con datos válidos")
    void dadoDatosValidos_cuandoSeConstruye_entoncesAtributosAsignados() {
      // Arrange, valores explícitos en formato texto
      String codigoIngresado = "PROD-42";
      String nombreIngresado = "Resaltador amarillo";
      String precioIngresado = "3.50";
      String cantidadIngresada = "100";

      double precioEsperado = 3.50;
      int cantidadEsperada = 100;

      // Act
      Producto nuevo =
          new Producto(codigoIngresado, nombreIngresado, precioIngresado, cantidadIngresada);

      // Assert, verificación simultánea de todos los atributos ya parseados
      assertAll(
          "Todos los atributos deben coincidir con los valores del constructor una vez parseados",
          () -> assertEquals(codigoIngresado, nuevo.getCodigo(), "codigo"),
          () -> assertEquals(nombreIngresado, nuevo.getNombre(), "nombre"),
          () -> assertEquals(precioEsperado, nuevo.getPrecio(), 0.001, "precio"),
          () -> assertEquals(cantidadEsperada, nuevo.getCantidad(), "cantidad"));
    }

    /** Verifica que {@code cantidad = 0} es un estado inicial legal. */
    @Test
    @DisplayName("Cantidad inicial cero es válida, producto sin stock")
    void dadoCantidadCero_cuandoSeConstruye_entoncesStockEsCero() {
      // Arrange
      String cantidadInicial = "0";

      // Act
      Producto sinStock =
          new Producto(CODIGO_VALIDO, NOMBRE_VALIDO, PRECIO_VALIDO, cantidadInicial);

      // Assert
      assertEquals(0, sinStock.getCantidad(), "El stock inicial de cero debe ser aceptado");
    }

    /**
     * Verifica que el producto construido con el fixture del {@code @BeforeEach} tiene exactamente
     * los atributos esperados una vez pasados por las conversiones de texto.
     */
    @Test
    @DisplayName("Fixture @BeforeEach refleja las constantes de prueba parseadas")
    void dadoFixture_entoncesCoincideConConstantesDePrueba() {
      assertAll(
          "El fixture debe coincidir con las constantes de prueba definidas en la clase",
          () -> assertEquals(CODIGO_VALIDO, producto.getCodigo()),
          () -> assertEquals(NOMBRE_VALIDO, producto.getNombre()),
          () -> assertEquals(Double.parseDouble(PRECIO_VALIDO), producto.getPrecio(), 0.001),
          () -> assertEquals(Integer.parseInt(CANTIDAD_VALIDA), producto.getCantidad()));
    }
  }

  // VALIDACIÓN DEL CAMPO: codigo

  /** Pruebas que verifican el rechazo de valores inválidos para el atributo {@code codigo}. */
  @Nested
  @DisplayName("2. Validación de 'codigo'")
  class ValidacionCodigo {

    @Test
    @DisplayName("Código vacío lanza IllegalArgumentException con mensaje sobre 'codigo'")
    void dadoCodigoVacio_cuandoSeConstruye_entoncesLanzaExcepcion() {
      // Arrange
      String codigoInvalido = "";

      // Act & Assert
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> new Producto(codigoInvalido, NOMBRE_VALIDO, PRECIO_VALIDO, CANTIDAD_VALIDA),
              "Debe lanzar excepción para código vacío");

      assertMensajeContiene(ex, "codigo");
    }

    @Test
    @DisplayName("Código null lanza IllegalArgumentException")
    void dadoCodigoNulo_cuandoSeConstruye_entoncesLanzaExcepcion() {
      // Arrange
      String codigoNulo = null;

      // Act & Assert
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> new Producto(codigoNulo, NOMBRE_VALIDO, PRECIO_VALIDO, CANTIDAD_VALIDA),
              "Debe lanzar excepción para código null");

      assertNotNull(ex.getMessage(), "El mensaje de error no debe ser nulo");
    }

    @ParameterizedTest(name = "codigo = \"{0}\" debe ser rechazado")
    @ValueSource(strings = {" ", "   ", "\t", "\n", "  \t  "})
    @DisplayName("Códigos solo con espacios en blanco son rechazados")
    void dadoCodigoEnBlanco_cuandoSeConstruye_entoncesLanzaExcepcion(String codigoBlanco) {
      assertThrows(
          IllegalArgumentException.class,
          () -> new Producto(codigoBlanco, NOMBRE_VALIDO, PRECIO_VALIDO, CANTIDAD_VALIDA),
          "Código en blanco debe ser rechazado: \"" + codigoBlanco + "\"");
    }
  }

  // VALIDACIÓN DEL CAMPO: precio

  /**
   * Pruebas que verifican el rechazo de valores inválidos para el atributo {@code precio}. Evalúa
   * no solo los límites del valor numérico, sino también los formatos del texto de entrada.
   */
  @Nested
  @DisplayName("3. Validación de 'precio'")
  class ValidacionPrecio {

    static Stream<Arguments> preciosInvalidosNumericos() {
      return Stream.of(
          Arguments.of("0.0", "cero exacto"),
          Arguments.of("-0.01", "negativo mínimo"),
          Arguments.of("-1.0", "negativo entero"),
          Arguments.of("-100.0", "negativo grande"),
          Arguments.of("-999.99", "negativo con decimales"));
    }

    @ParameterizedTest(name = "precio numérico {1} ({0}) → IllegalArgumentException")
    @MethodSource("preciosInvalidosNumericos")
    @DisplayName("Precios numéricos ≤ 0 lanzan IllegalArgumentException con mensaje sobre 'precio'")
    void dadoPrecioInvalidoNumerico_cuandoSeConstruye_entoncesLanzaExcepcion(
        String precioInvalido, String descripcion) {
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> new Producto(CODIGO_VALIDO, NOMBRE_VALIDO, precioInvalido, CANTIDAD_VALIDA),
              "Debe rechazar precio " + descripcion + ": " + precioInvalido);

      assertMensajeContiene(ex, "precio");
    }

    static Stream<Arguments> preciosNoNumericos() {
      return Stream.of(
          Arguments.of("abc", "texto alfabético"),
          Arguments.of("", "cadena vacía"),
          Arguments.of("   ", "solo espacios"),
          Arguments.of("25,00", "separador decimal inválido (coma)"));
    }

    @ParameterizedTest(name = "precio formato {1} ({0}) → IllegalArgumentException")
    @MethodSource("preciosNoNumericos")
    @DisplayName(
        "Precios con formato no numérico lanzan IllegalArgumentException atrapada por try/catch")
    void dadoPrecioFormatoInvalido_cuandoSeConstruye_entoncesLanzaExcepcion(
        String precioInvalido, String descripcion) {
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> new Producto(CODIGO_VALIDO, NOMBRE_VALIDO, precioInvalido, CANTIDAD_VALIDA),
              "Debe rechazar precio con formato inválido " + descripcion);

      assertMensajeContiene(ex, "numerico");
    }

    @Test
    @DisplayName("Precio mínimo positivo (0.01) en formato texto es aceptado")
    void dadoPrecioMinimoPositivo_cuandoSeConstruye_entoncesEsAceptado() {
      // Arrange
      String precioMinimo = "0.01";

      // Act
      Producto p = new Producto(CODIGO_VALIDO, NOMBRE_VALIDO, precioMinimo, CANTIDAD_VALIDA);

      // Assert
      assertEquals(0.01, p.getPrecio(), 0.001);
    }

    /**
     * Documenta un límite de la clase actual: Dado que {@code Double.parseDouble(null)} lanza un
     * {@code NullPointerException} nativo (que no hereda de {@code NumberFormatException}), este no
     * es capturado por el bloque catch de la clase Producto, dejando pasar la excepción hacia
     * arriba.
     */
    @Test
    @DisplayName(
        "Precio null lanza NullPointerException (comportamiento no envuelto de Double.parseDouble)")
    void dadoPrecioNulo_cuandoSeConstruye_entoncesLanzaNPE() {
      assertThrows(
          NullPointerException.class,
          () -> new Producto(CODIGO_VALIDO, NOMBRE_VALIDO, null, CANTIDAD_VALIDA),
          "Double.parseDouble no envuelve null en NumberFormatException, propagando la excepción"
              + " nativa");
    }
  }

  // VALIDACIÓN DEL CAMPO: cantidad

  /**
   * Pruebas que verifican el rechazo de valores inválidos o con formatos erróneos para el atributo
   * {@code cantidad}.
   */
  @Nested
  @DisplayName("4. Validación de 'cantidad'")
  class ValidacionCantidad {

    static Stream<Arguments> cantidadesNegativas() {
      return Stream.of(
          Arguments.of("-1", "menos uno"),
          Arguments.of("-10", "menos diez"),
          Arguments.of("-100", "menos cien"),
          Arguments.of(String.valueOf(Integer.MIN_VALUE), "mínimo entero negativo"));
    }

    @ParameterizedTest(name = "cantidad {1} ({0}) → IllegalArgumentException")
    @MethodSource("cantidadesNegativas")
    @DisplayName(
        "Cantidades negativas lanzan IllegalArgumentException con mensaje sobre 'cantidad'")
    void dadoCantidadNegativa_cuandoSeConstruye_entoncesLanzaExcepcion(
        String cantidadNegativa, String descripcion) {
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> new Producto(CODIGO_VALIDO, NOMBRE_VALIDO, PRECIO_VALIDO, cantidadNegativa),
              "Debe rechazar cantidad " + descripcion + ": " + cantidadNegativa);

      assertMensajeContiene(ex, "cantidad");
    }

    static Stream<Arguments> cantidadesNoNumericas() {
      return Stream.of(
          Arguments.of("abc", "texto alfabético"),
          Arguments.of("", "cadena vacía"),
          Arguments.of("   ", "solo espacios"),
          Arguments.of("10.5", "valor con decimales"),
          Arguments.of(null, "valor nulo (Integer.parseInt sí lo envuelve en NFE)"));
    }

    @ParameterizedTest(name = "cantidad formato {1} ({0}) → IllegalArgumentException")
    @MethodSource("cantidadesNoNumericas")
    @DisplayName("Cantidades con formato no numérico o nulo lanzan IllegalArgumentException")
    void dadoCantidadFormatoInvalido_cuandoSeConstruye_entoncesLanzaExcepcion(
        String cantidadInvalida, String descripcion) {
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> new Producto(CODIGO_VALIDO, NOMBRE_VALIDO, PRECIO_VALIDO, cantidadInvalida),
              "Debe rechazar cantidad con formato inválido " + descripcion);

      assertMensajeContiene(ex, "numerico");
    }

    @Test
    @DisplayName("Cantidad negativa es rechazada independientemente del precio")
    void dadoCantidadNegativaConPrecioAlto_cuandoSeConstruye_entoncesLanzaExcepcion() {
      // Arrange
      String precioAlto = "999999.99";
      String cantidadNegativa = "-1";

      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> new Producto(CODIGO_VALIDO, NOMBRE_VALIDO, precioAlto, cantidadNegativa));
    }
  }

  // COMBINACIONES DE ENTRADAS INVÁLIDAS

  /**
   * Pruebas que verifican que la validación funciona correctamente cuando varios campos (como
   * texto) son inválidos simultáneamente.
   */
  @Nested
  @DisplayName("5. Combinaciones de entradas inválidas")
  class CombinacionesInvalidas {

    static Stream<Arguments> combinacionesInvalidas() {
      return Stream.of(
          // codigo  , nombre       , precio , cantidad , descripcion
          Arguments.of("", NOMBRE_VALIDO, PRECIO_VALIDO, "-1", "codigo vacío + cantidad negativa"),
          Arguments.of(null, NOMBRE_VALIDO, "0.0", "10", "codigo null + precio cero"),
          Arguments.of("P1", NOMBRE_VALIDO, "-5.0", "-5", "precio negativo + cantidad negativa"),
          Arguments.of(
              "P2", NOMBRE_VALIDO, "abc", "xyz", "precio no numérico + cantidad no numérica"));
    }

    @ParameterizedTest(name = "{4}")
    @MethodSource("combinacionesInvalidas")
    @DisplayName("Combinaciones inválidas siempre lanzan IllegalArgumentException")
    void dadaCombinacionInvalida_cuandoSeConstruye_entoncesLanzaExcepcion(
        String codigo, String nombre, String precio, String cantidad, String descripcion) {
      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> new Producto(codigo, nombre, precio, cantidad),
          "La combinación inválida '" + descripcion + "' debe ser rechazada");
    }
  }

  // MÉTODOS HELPER PRIVADOS

  /**
   * Verifica que el mensaje de la excepción proporcionada contiene la subcadena esperada (sin
   * distinción de mayúsculas).
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
