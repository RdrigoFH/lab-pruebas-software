package com.lab04;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.lenient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("CarritoCompra - Pruebas Unitarias")
class CarritoCompraTest {

  @Mock
  private ServicioPrecio servicioPrecio;

  private CarritoCompra carrito;
  private Producto producto1;
  private Producto producto2;
  private Producto productoSinStock;

  @BeforeEach
  void setUp() {
   

    carrito = new CarritoCompra(servicioPrecio);

    producto1 = new Producto("P001", "Laptop", 1500.00, true);
    producto2 = new Producto("P002", "Mouse", 25.00, true);
    productoSinStock = new Producto("P003", "Teclado", 80.00, false);
  }

  @AfterEach
  void tearDown() {
    carrito.vaciarCarrito();
  }

  @Nested
  @DisplayName("Pruebas de Agregar Productos")
  class AgregarProductosTests {

    @Test
    @DisplayName("Debe agregar un producto correctamente")
    void debeAgregarProductoCorrectamente() {
      carrito.agregarProducto(producto1, 1);

      assertEquals(1, carrito.getItems().size());
      assertTrue(carrito.contieneProducto(producto1));
      assertEquals(1, carrito.getCantidadTotalProductos());
    }

    @Test
    @DisplayName("Debe lanzar excepción al agregar producto nulo")
    void debeLanzarExcepcionAlAgregarProductoNulo() {
      assertThrows(IllegalArgumentException.class,
          () -> carrito.agregarProducto(null, 1));
    }

    @Test
    @DisplayName("Debe lanzar excepción al agregar cantidad negativa")
    void debeLanzarExcepcionAlAgregarCantidadNegativa() {
      assertThrows(IllegalArgumentException.class,
          () -> carrito.agregarProducto(producto1, -5));
    }

    @Test
    @DisplayName("Debe lanzar excepción al agregar cantidad cero")
    void debeLanzarExcepcionAlAgregarCantidadCero() {
      assertThrows(IllegalArgumentException.class,
          () -> carrito.agregarProducto(producto1, 0));
    }

    @Test
    @DisplayName("Debe lanzar excepción al agregar producto no disponible")
    void debeLanzarExcepcionAlAgregarProductoNoDisponible() {
      assertThrows(IllegalStateException.class,
          () -> carrito.agregarProducto(productoSinStock, 1));
    }

    @Test
    @DisplayName("Debe acumular cantidades cuando se agrega el mismo producto")
    void debeAcumularCantidadesCuandoSeAgregaElMismoProducto() {
      carrito.agregarProducto(producto1, 2);
      carrito.agregarProducto(producto1, 3);

      assertEquals(1, carrito.getItems().size());
      assertEquals(5, carrito.getCantidadTotalProductos());
    }
  }

  @Nested
  @DisplayName("Pruebas de Remover Productos")
  class RemoverProductosTests {

    @BeforeEach
    void setupCarrito() {
      carrito.agregarProducto(producto1, 5);
      carrito.agregarProducto(producto2, 3);
    }

    @Test
    @DisplayName("Debe remover parte de la cantidad de un producto")
    void debeRemoverCantidadParcial() {
      carrito.removerProducto(producto1, 2);

      assertEquals(2, carrito.getItems().size());
      assertEquals(3, carrito.getItems().get(0).getCantidad());
      assertEquals(6, carrito.getCantidadTotalProductos());
    }

    @Test
    @DisplayName("Debe eliminar completamente un producto al remover toda la cantidad")
    void debeEliminarCompletamenteUnProducto() {
      carrito.removerProducto(producto1, 5);

      assertEquals(1, carrito.getItems().size());
      assertFalse(carrito.contieneProducto(producto1));
    }

    @Test
    @DisplayName("Debe lanzar excepción al remover producto inexistente")
    void debeLanzarExcepcionAlRemoverProductoInexistente() {
      Producto productoInexistente = new Producto("P999", "Inexistente", 100, true);

      assertThrows(IllegalStateException.class,
          () -> carrito.removerProducto(productoInexistente, 1));
    }

    @Test
    @DisplayName("Debe lanzar excepción al remover cantidad negativa")
    void debeLanzarExcepcionAlRemoverCantidadNegativa() {
      assertThrows(IllegalArgumentException.class,
          () -> carrito.removerProducto(producto1, -1));
    }
  }

  @Nested
  @DisplayName("Pruebas de Cálculo de Totales")
  class CalculosTotalesTests {

    @Test
    @DisplayName("Carrito vacío debe tener total = 0")
    void carritoVacioDebeTenerTotalCero() {
      assertEquals(0.0, carrito.calcularSubtotal());
      assertEquals(0.0, carrito.calcularTotal());
    }

    @Test
    @DisplayName("Debe calcular subtotal correctamente con múltiples productos")
    void debeCalcularSubtotalCorrectamenteConMultiplesProductos() {
      carrito.agregarProducto(producto1, 2);
      carrito.agregarProducto(producto2, 3);

      assertEquals(3075.0, carrito.calcularSubtotal());
    }

    @Test
    @DisplayName("Debe calcular total con descuento e impuesto usando mocks")
    void debeCalcularTotalConDescuentoEImpuestoUsandoMocks() {
        lenient().when(servicioPrecio.calcularDescuento(anyDouble())).thenReturn(100.0);
        lenient().when(servicioPrecio.calcularImpuesto(anyDouble())).thenReturn(200.0);

        carrito.agregarProducto(producto1, 1);
        assertEquals(1600.0, carrito.calcularTotal());

    }
  }

  @Nested
  @DisplayName("Pruebas de Productos Duplicados")
  class ProductosDuplicadosTests {

    @Test
    @DisplayName("Debe detectar y manejar productos duplicados (acumulación)")
    void debeDetectarYManejarProductosDuplicados() {
      carrito.agregarProducto(producto1, 1);
      carrito.agregarProducto(producto1, 1);
      carrito.agregarProducto(producto1, 2);

      assertEquals(1, carrito.getItems().size());
      assertEquals(4, carrito.getCantidadTotalProductos());
    }

    @Test
    @DisplayName("Producto duplicado debe mantener un solo item")
    void productoDuplicadoDebeMantenerUnSoloItem() {
      carrito.agregarProducto(producto1, 1);
      carrito.agregarProducto(producto1, 1);
      carrito.agregarProducto(producto1, 2);

      assertEquals(1, carrito.getItems().size());
      assertEquals(4, carrito.getItems().get(0).getCantidad());
    }
  }

  @Nested
  @DisplayName("Pruebas de Casos Límite")
  class CasosLimiteTests {

    @Test
    @DisplayName("Carrito con 1 producto debe funcionar correctamente")
    void carritoConUnProductoDebeFuncionarCorrectamente() {
      carrito.agregarProducto(producto1, 1);

      assertEquals(1, carrito.getItems().size());
      assertEquals(producto1.getPrecio(), carrito.calcularSubtotal());
    }

    @Test
    @DisplayName("Múltiples operaciones deben mantener consistencia")
    void multiplesOperacionesDebenMantenerConsistencia() {
      for (int i = 0; i < 100; i++) {
        carrito.agregarProducto(producto1, 1);
      }
      assertEquals(100, carrito.getCantidadTotalProductos());

      for (int i = 0; i < 50; i++) {
        carrito.removerProducto(producto1, 1);
      }
      assertEquals(50, carrito.getCantidadTotalProductos());
    }

    @Test
    @DisplayName("Debe mantener historial de operaciones")
    void debeMantenerHistorialDeOperaciones() {
      carrito.agregarProducto(producto1, 1);
      carrito.agregarProducto(producto2, 2);
      carrito.removerProducto(producto1, 1);

      assertTrue(carrito.getHistorialOperaciones().size() >= 4);
    }

    @Test
    @DisplayName("Vaciar carrito debe eliminar todos los items")
    void vaciarCarritoDebeEliminarTodosLosItems() {
      carrito.agregarProducto(producto1, 5);
      carrito.agregarProducto(producto2, 3);

      assertEquals(2, carrito.getItems().size());

      carrito.vaciarCarrito();

      assertEquals(0, carrito.getItems().size());
      assertEquals(0.0, carrito.calcularSubtotal());
    }
  }
}