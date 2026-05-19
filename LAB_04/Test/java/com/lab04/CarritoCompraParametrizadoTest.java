package com.lab04;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("CarritoCompra - Pruebas Parametrizadas")
class CarritoCompraParametrizadoTest {

  @Mock
  private ServicioPrecio servicioPrecio;

  private CarritoCompra carrito;
  private Producto laptop;
  private Producto mouse;

  @BeforeEach
  void setUp() {
    carrito = new CarritoCompra(servicioPrecio);
    laptop = new Producto("P001", "Laptop", 1500.00, true);
    mouse = new Producto("P002", "Mouse", 25.00, true);
  }

  @ParameterizedTest
  @DisplayName("Agregar múltiples cantidades del mismo producto debe acumular")
  @ValueSource(ints = {1, 2, 3, 4, 5, 10, 20, 50})
  void agregarMultiplesCantidadesDebeAcumular(int cantidad) {
    for (int i = 0; i < cantidad; i++) {
      carrito.agregarProducto(laptop, 1);
    }

    assertEquals(cantidad, carrito.getCantidadTotalProductos());
  }

  @ParameterizedTest
  @DisplayName("Cálculo de subtotal para diferentes cantidades")
  @CsvSource({
      "1, 1500.00",
      "2, 3000.00",
      "3, 4500.00",
      "5, 7500.00",
      "10, 15000.00"
  })
  void calculoSubtotalParaDiferentesCantidades(int cantidad, double subtotalEsperado) {
    carrito.agregarProducto(laptop, cantidad);

    assertEquals(subtotalEsperado, carrito.calcularSubtotal());
  }

@ParameterizedTest
@DisplayName("Cálculo de total con descuento e impuesto para diferentes cantidades")
@CsvSource({
    "1, 10, 19, 1509.00",
    "2, 50, 190, 3140.00",
    "3, 100, 380, 4780.00",
    "4, 200, 760, 6560.00" 
})
void calculoTotalConDescuentoImpuesto(int cantidad, double descuento,
                                      double impuesto, double totalEsperado) {
  when(servicioPrecio.calcularDescuento(anyDouble())).thenReturn(descuento);
  when(servicioPrecio.calcularImpuesto(anyDouble())).thenReturn(impuesto);

  carrito.agregarProducto(laptop, cantidad);
  assertEquals(totalEsperado, carrito.calcularTotal(), 0.01);
}



  @ParameterizedTest
  @DisplayName("Múltiples operaciones de agregar y remover")
  @CsvSource({
      "5, 2, 3",
      "10, 5, 5",
      "20, 15, 5",
      "100, 60, 40"
  })
  void multiplesOperacionesAgregarRemover(int agregar, int remover, int esperado) {
    carrito.agregarProducto(mouse, agregar);
    carrito.removerProducto(mouse, remover);

    assertEquals(esperado, carrito.getCantidadTotalProductos());
  }

  @ParameterizedTest
  @DisplayName("Cálculo de total para diferentes combinaciones de productos")
  @CsvSource({
      "1, 0, 1500.00",
      "0, 3, 75.00",
      "2, 2, 3050.00",
      "1, 5, 1625.00"
  })
  void calculoTotalParaDiferentesCombinaciones(int cantLaptop, int cantMouse,
                                                double totalEsperado) {
    when(servicioPrecio.calcularDescuento(anyDouble())).thenReturn(0.0);
    when(servicioPrecio.calcularImpuesto(anyDouble())).thenReturn(0.0);

    if (cantLaptop > 0) {
      carrito.agregarProducto(laptop, cantLaptop);
    }
    if (cantMouse > 0) {
      carrito.agregarProducto(mouse, cantMouse);
    }

    assertEquals(totalEsperado, carrito.calcularTotal());
  }
}