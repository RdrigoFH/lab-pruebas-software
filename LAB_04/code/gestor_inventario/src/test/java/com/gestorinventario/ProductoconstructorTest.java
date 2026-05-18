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
 * bajo entradas válidas y que rechaza, cualquier dato inválido en sus atributos fundamentales:
 * {@code codigo}, {@code nombre}, {@code precio} y {@code cantidad}.
 */
@DisplayName("Producto Construcción y validación de atributos")
class ProductoconstructorTest {

  // Constantes de prueba, valores canónicos reutilizados en toda la clase

  /** Código de producto válido usado como referencia en los tests felices. */
  private static final String CODIGO_VALIDO = "P001";

  /** Nombre de producto válido usado como referencia en los tests felices. */
  private static final String NOMBRE_VALIDO = "Cuaderno A4";

  /** Precio positivo válido usado como referencia en los tests felices. */
  private static final double PRECIO_VALIDO = 25.00;

  /** Cantidad inicial válida (mayor que cero) usada en los tests felices. */
  private static final int CANTIDAD_VALIDA = 10;

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
   * cumplen las reglas de negocio.
   *
   * <p>Comenzamos siempre con el <em>happy path</em> porque define el contrato mínimo que la
   * implementación debe cumplir. Si este grupo falla, el resto de los grupos carece de sentido.
   */
  @Nested
  @DisplayName("1. Escenarios felices, construcción válida")
  class EscenariosFelicess {

    /**
     * Verifica que el constructor asigna correctamente todos los atributos cuando los datos de
     * entrada son válidos.
     *
     * <p>Usamos {@code assertAll} para reportar en una sola ejecución cualquier atributo que no
     * haya sido asignado correctamente, en lugar de abortar al primer fallo.
     */
    @Test
    @DisplayName("Constructor asigna todos los atributos con datos válidos")
    void dadoDatosValidos_cuandoSeConstruye_entoncesAtributosAsignados() {
      // Arrange, valores explícitos
      String codigoEsperado = "PROD-42";
      String nombreEsperado = "Resaltador amarillo";
      double precioEsperado = 3.50;
      int cantidadEsperada = 100;

      // Act
      Producto nuevo =
          new Producto(codigoEsperado, nombreEsperado, precioEsperado, cantidadEsperada);

      // Assert, verificación simultánea de todos los atributos
      assertAll(
          "Todos los atributos deben coincidir con los valores del constructor",
          () -> assertEquals(codigoEsperado, nuevo.getCodigo(), "codigo"),
          () -> assertEquals(nombreEsperado, nuevo.getNombre(), "nombre"),
          () -> assertEquals(precioEsperado, nuevo.getPrecio(), 0.001, "precio"),
          () -> assertEquals(cantidadEsperada, nuevo.getCantidad(), "cantidad"));
    }

    /**
     * Verifica que {@code cantidad = 0} es un estado inicial legal.
     *
     * <p>Un almacén puede perfectamente registrar un producto del que aún no tiene unidades en
     * stock. Este caso límite debe ser permitido, a diferencia de {@code cantidad < 0}.
     */
    @Test
    @DisplayName("Cantidad inicial cero es válida, producto sin stock")
    void dadoCantidadCero_cuandoSeConstruye_entoncesStockEsCero() {
      // Arrange
      int cantidadInicial = 0;

      // Act
      Producto sinStock =
          new Producto(CODIGO_VALIDO, NOMBRE_VALIDO, PRECIO_VALIDO, cantidadInicial);

      // Assert
      assertEquals(0, sinStock.getCantidad(), "El stock inicial de cero debe ser aceptado");
    }

    /**
     * Verifica que el producto construido con el fixture del {@code @BeforeEach} tiene exactamente
     * los atributos esperados.
     *
     * <p>Este test protege al fixture: si alguien modifica {@code inicializarProductoBase()} sin
     * actualizar las constantes, este caso fallará de inmediato.
     */
    @Test
    @DisplayName("Fixture @BeforeEach refleja las constantes de prueba")
    void dadoFixture_entoncesCoincideConConstantesDePrueba() {
      // Assert, no requiere Act porque el fixture ya fue construido
      assertAll(
          "El fixture debe coincidir con las constantes de prueba definidas en la clase",
          () -> assertEquals(CODIGO_VALIDO, producto.getCodigo()),
          () -> assertEquals(NOMBRE_VALIDO, producto.getNombre()),
          () -> assertEquals(PRECIO_VALIDO, producto.getPrecio(), 0.001),
          () -> assertEquals(CANTIDAD_VALIDA, producto.getCantidad()));
    }
  }

  // VALIDACIÓN DEL CAMPO: codigo

  /**
   * Pruebas que verifican el rechazo de valores inválidos para el atributo {@code codigo}.
   *
   * <p>El requisito establece que el código no debe ser vacío. Por extensión, consideramos que
   * tampoco debe ser {@code null} ni contener únicamente espacios en blanco, ya que estos casos son
   * semánticamente equivalentes a "sin código" y romperían la identificación unívoca del producto.
   */
  @Nested
  @DisplayName("2. Validación de 'codigo'")
  class ValidacionCodigo {

    /**
     * Verifica que un código vacío ({@code ""}) lanza {@link IllegalArgumentException} con un
     * mensaje que identifica el campo afectado.
     */
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

    /**
     * Verifica que un código {@code null} lanza {@link IllegalArgumentException}.
     *
     * <p>Aunque {@code null} y {@code ""} son técnicamente distintos, ambos deben ser rechazados
     * porque representan ausencia de identificador.
     */
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

    /**
     * Verifica que códigos compuestos solo por espacios en blanco son rechazados.
     *
     * <p>Este caso complementa las validaciones anteriores: una cadena de espacios pasa la
     * verificación {@code isEmpty()} pero semánticamente está vacía. La implementación debería usar
     * {@code isBlank()} o {@code trim().isEmpty()}.
     *
     * @param codigoBlanco distintos patrones de espacios en blanco
     */
    @ParameterizedTest(name = "codigo = \"{0}\" debe ser rechazado")
    @ValueSource(strings = {" ", "   ", "\t", "\n", "  \t  "})
    @DisplayName("Códigos solo con espacios en blanco son rechazados")
    void dadoCodigoEnBlanco_cuandoSeConstruye_entoncesLanzaExcepcion(String codigoBlanco) {
      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> new Producto(codigoBlanco, NOMBRE_VALIDO, PRECIO_VALIDO, CANTIDAD_VALIDA),
          "Código en blanco debe ser rechazado: \"" + codigoBlanco + "\"");
    }
  }

  // VALIDACIÓN DEL CAMPO: precio

  /**
   * Pruebas que verifican el rechazo de valores inválidos para el atributo {@code precio}.
   *
   * <p>El requisito dice explícitamente que el precio debe ser positivo. Esto excluye tanto valores
   * negativos como el valor cero, que carece de sentido comercial para un producto en inventario.
   */
  @Nested
  @DisplayName("3. Validación de 'precio'")
  class ValidacionPrecio {

    /**
     * Proveedor de datos para precios inválidos usados en las pruebas parametrizadas de esta
     * sección.
     *
     * <p>Se usa {@code @MethodSource} en lugar de {@code @CsvSource} para poder asociar a cada
     * valor una descripción legible que mejora el reporte de JUnit.
     *
     * @return stream de argumentos con precio inválido y su descripción
     */
    static Stream<Arguments> preciosInvalidos() {
      return Stream.of(
          Arguments.of(0.0, "cero exacto"),
          Arguments.of(-0.01, "negativo mínimo"),
          Arguments.of(-1.0, "negativo entero"),
          Arguments.of(-100.0, "negativo grande"),
          Arguments.of(-999.99, "negativo con decimales"));
    }

    /**
     * Verifica que cualquier precio menor o igual a cero lanza {@link IllegalArgumentException} con
     * un mensaje que identifica el campo.
     *
     * <p>Consolidamos todos los casos inválidos de precio en una sola prueba parametrizada para
     * evitar duplicar la misma lógica de aserción y mantener la suite escalable ante nuevos valores
     * límite.
     *
     * @param precioInvalido el valor de precio que debe ser rechazado
     * @param descripcion descripción legible del caso, visible en el reporte
     */
    @ParameterizedTest(name = "precio {1} ({0}) → IllegalArgumentException")
    @MethodSource("preciosInvalidos")
    @DisplayName("Precios ≤ 0 lanzan IllegalArgumentException con mensaje sobre 'precio'")
    void dadoPrecioInvalido_cuandoSeConstruye_entoncesLanzaExcepcion(
        double precioInvalido, String descripcion) {
      // Act & Assert
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> new Producto(CODIGO_VALIDO, NOMBRE_VALIDO, precioInvalido, CANTIDAD_VALIDA),
              "Debe rechazar precio " + descripcion + ": " + precioInvalido);

      assertMensajeContiene(ex, "precio");
    }

    /**
     * Verifica que el precio mínimo positivo aceptado es cualquier valor {@code > 0}, incluyendo
     * precios muy pequeños con decimales.
     *
     * <p>Este caso complementa los anteriores confirmando que el umbral de aceptación es
     * estrictamente {@code precio > 0}, sin ningún mínimo adicional.
     */
    @Test
    @DisplayName("Precio mínimo positivo (0.01) es aceptado")
    void dadoPrecioMinimoPositivo_cuandoSeConstruye_entoncesEsAceptado() {
      // Arrange
      double precioMinimo = 0.01;

      // Act
      Producto p = new Producto(CODIGO_VALIDO, NOMBRE_VALIDO, precioMinimo, CANTIDAD_VALIDA);

      // Assert
      assertEquals(precioMinimo, p.getPrecio(), 0.001);
    }
  }

  // VALIDACIÓN DEL CAMPO: cantidad

  /**
   * Pruebas que verifican el rechazo de valores negativos para el atributo {@code cantidad}.
   *
   * <p>Cantidad cero es un caso válido (tratado en la sección 1), pero cualquier valor negativo
   * indica un estado inconsistente que nunca debería persistir en el modelo de dominio.
   */
  @Nested
  @DisplayName("4. Validación de 'cantidad'")
  class ValidacionCantidad {

    /**
     * Proveedor de datos para cantidades negativas usadas en la prueba parametrizada.
     *
     * @return stream de argumentos con cantidad negativa y descripción
     */
    static Stream<Arguments> cantidadesNegativas() {
      return Stream.of(
          Arguments.of(-1, "menos uno"),
          Arguments.of(-10, "menos diez"),
          Arguments.of(-100, "menos cien"),
          Arguments.of(Integer.MIN_VALUE, "mínimo entero negativo"));
    }

    /**
     * Verifica que cualquier cantidad negativa en el constructor lanza {@link
     * IllegalArgumentException} con un mensaje que identifica el campo.
     *
     * @param cantidadNegativa el valor negativo que debe ser rechazado
     * @param descripcion descripción del caso para el reporte de JUnit
     */
    @ParameterizedTest(name = "cantidad {1} ({0}) → IllegalArgumentException")
    @MethodSource("cantidadesNegativas")
    @DisplayName(
        "Cantidades negativas lanzan IllegalArgumentException con mensaje sobre 'cantidad'")
    void dadoCantidadNegativa_cuandoSeConstruye_entoncesLanzaExcepcion(
        int cantidadNegativa, String descripcion) {
      // Act & Assert
      IllegalArgumentException ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> new Producto(CODIGO_VALIDO, NOMBRE_VALIDO, PRECIO_VALIDO, cantidadNegativa),
              "Debe rechazar cantidad " + descripcion + ": " + cantidadNegativa);

      assertMensajeContiene(ex, "cantidad");
    }

    /**
     * Verifica que la cantidad es independiente del precio: incluso con precio muy alto, una
     * cantidad negativa sigue siendo rechazada.
     *
     * <p>Este test protege contra implementaciones que podrían omitir la validación de cantidad al
     * detectar que otros atributos son válidos.
     */
    @Test
    @DisplayName("Cantidad negativa es rechazada independientemente del precio")
    void dadoCantidadNegativaConPrecioAlto_cuandoSeConstruye_entoncesLanzaExcepcion() {
      // Arrange
      double precioAlto = 999_999.99;
      int cantidadNegativa = -1;

      // Act & Assert
      assertThrows(
          IllegalArgumentException.class,
          () -> new Producto(CODIGO_VALIDO, NOMBRE_VALIDO, precioAlto, cantidadNegativa));
    }
  }

  // COMBINACIONES DE ENTRADAS INVÁLIDAS (Múltiples campos)

  /**
   * Pruebas que verifican que la validación funciona correctamente cuando varios campos son
   * inválidos simultáneamente.
   *
   * <p>Aunque en la práctica basta con que el constructor falle en el primer campo inválido que
   * encuentre, estos tests aseguran que ninguna combinación de datos corruptos logra producir un
   * objeto en estado inconsistente.
   */
  @Nested
  @DisplayName("5. Combinaciones de entradas inválidas")
  class CombinacionesInvalidas {

    /**
     * Proveedor de datos para combinaciones de argumentos completamente inválidos.
     *
     * @return stream de argumentos con todas las combinaciones a probar
     */
    static Stream<Arguments> combinacionesInvalidas() {
      return Stream.of(
          // codigo  , nombre       , precio , cantidad , descripcion
          Arguments.of("", NOMBRE_VALIDO, PRECIO_VALIDO, -1, "codigo vacío + cantidad negativa"),
          Arguments.of(null, NOMBRE_VALIDO, 0.0, 10, "codigo null + precio cero"),
          Arguments.of("P1", NOMBRE_VALIDO, -5.0, -5, "precio negativo + cantidad negativa"));
    }

    /**
     * Verifica que cualquier combinación de argumentos inválidos produce una {@link
     * IllegalArgumentException}.
     *
     * <p>No validamos cuál campo falla primero; solo verificamos que el objeto nunca llega a
     * construirse con datos corruptos.
     *
     * @param codigo código del producto (puede ser inválido)
     * @param nombre nombre del producto
     * @param precio precio del producto (puede ser inválido)
     * @param cantidad cantidad del producto (puede ser inválida)
     * @param descripcion descripción del escenario para el reporte
     */
    @ParameterizedTest(name = "{4}")
    @MethodSource("combinacionesInvalidas")
    @DisplayName("Combinaciones inválidas siempre lanzan IllegalArgumentException")
    void dadaCombinacionInvalida_cuandoSeConstruye_entoncesLanzaExcepcion(
        String codigo, String nombre, double precio, int cantidad, String descripcion) {
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
   * <p>Centralizar esta aserción evita duplicar la lógica de verificación de mensajes en cada test
   * y facilita ajustar el criterio de validación en un único lugar si el formato del mensaje
   * cambia.
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
