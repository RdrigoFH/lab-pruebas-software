package com.gestorinventario;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
  }
}
