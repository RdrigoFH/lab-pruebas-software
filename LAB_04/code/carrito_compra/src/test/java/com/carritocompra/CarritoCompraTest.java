package com.carritocompra;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("CarritoCompra — pruebas sin mocks")
class CarritoCompraTest {

  private static final ServicioPrecio SIN_CARGOS =
      new ServicioPrecio() {
        @Override
        public double calcularDescuento(double monto) {
          return 0.0;
        }

        @Override
        public double calcularImpuesto(double monto) {
          return 0.0;
        }
      };

  private Producto laptop;
  private Producto mouse;
  private Producto productoNoDisponible;
  private CarritoCompra carrito;

  @BeforeEach
  void setUp() {
    laptop = new Producto("P001", "Laptop", 1000.0, true);
    mouse = new Producto("P002", "Mouse", 25.0, true);
    productoNoDisponible = new Producto("P003", "Teclado", 50.0, false);
    carrito = new CarritoCompra(SIN_CARGOS);
  }

  // -------------------------------------------------------------------------
  // Construcción
  // -------------------------------------------------------------------------

  @Nested
  @DisplayName("Construcción del carrito")
  class Construccion {

    @Test
    @DisplayName("lanza excepción cuando servicioPrecio es nulo")
    void constructorServicioPrecioNulo() {
      assertThrows(IllegalArgumentException.class, () -> new CarritoCompra(null));
    }

    @Test
    @DisplayName("carrito nuevo está vacío y tiene entrada en el historial")
    void carritoNuevoVacio() {
      assertTrue(carrito.getItems().isEmpty());
      assertFalse(carrito.getHistorialOperaciones().isEmpty());
    }
  }

  // -------------------------------------------------------------------------
  // Agregar productos
  // -------------------------------------------------------------------------

  @Nested
  @DisplayName("Agregar productos")
  class AgregarProductos {

    @Test
    @DisplayName("agrega un producto disponible correctamente")
    void agregarProductoDisponible() {
      carrito.agregarProducto(laptop, 1);

      assertTrue(carrito.contieneProducto(laptop));
      assertEquals(1, carrito.getCantidadTotalProductos());
    }

    @Test
    @DisplayName("actualiza la cantidad al agregar un producto duplicado")
    void agregarProductoDuplicadoActualizaCantidad() {
      carrito.agregarProducto(laptop, 2);
      carrito.agregarProducto(laptop, 3);

      assertEquals(1, carrito.getItems().size());
      assertEquals(3, carrito.getItems().get(0).getCantidad());
      assertEquals(3, carrito.getCantidadTotalProductos());
    }

    @Test
    @DisplayName("lanza excepción al agregar producto nulo")
    void agregarProductoNulo() {
      assertThrows(IllegalArgumentException.class, () -> carrito.agregarProducto(null, 1));
    }

    @Test
    @DisplayName("lanza excepción al agregar cantidad negativa")
    void agregarCantidadNegativa() {
      assertThrows(IllegalArgumentException.class, () -> carrito.agregarProducto(laptop, -1));
    }

    @Test
    @DisplayName("lanza excepción al agregar cantidad cero")
    void agregarCantidadCero() {
      assertThrows(IllegalArgumentException.class, () -> carrito.agregarProducto(laptop, 0));
    }

    @Test
    @DisplayName("lanza excepción al agregar producto no disponible")
    void agregarProductoNoDisponible() {
      assertThrows(
          IllegalStateException.class, () -> carrito.agregarProducto(productoNoDisponible, 1));
    }

    @Test
    @DisplayName("registra operación en historial al agregar")
    void agregarRegistraHistorial() {
      int tamanoAntes = carrito.getHistorialOperaciones().size();
      carrito.agregarProducto(laptop, 1);

      assertTrue(carrito.getHistorialOperaciones().size() > tamanoAntes);
    }
  }

  // -------------------------------------------------------------------------
  // Remover productos
  // -------------------------------------------------------------------------

  @Nested
  @DisplayName("Remover productos")
  class RemoverProductos {

    @Test
    @DisplayName("elimina el producto del carrito")
    void removerProductoExistenteEliminaItem() {
      carrito.agregarProducto(laptop, 3);
      carrito.removerProducto(laptop);

      assertFalse(carrito.contieneProducto(laptop));
      assertTrue(carrito.getItems().isEmpty());
      assertEquals(0, carrito.getCantidadTotalProductos());
    }

    @Test
    @DisplayName("remueve solo el producto indicado")
    void removerProductoMantieneOtrosItems() {
      carrito.agregarProducto(laptop, 1);
      carrito.agregarProducto(mouse, 2);

      carrito.removerProducto(laptop);

      assertFalse(carrito.contieneProducto(laptop));
      assertTrue(carrito.contieneProducto(mouse));
      assertEquals(2, carrito.getCantidadTotalProductos());
    }

    @Test
    @DisplayName("lanza excepción al remover producto nulo")
    void removerProductoNulo() {
      assertThrows(IllegalArgumentException.class, () -> carrito.removerProducto(null));
    }

    @Test
    @DisplayName("producto inexistente no modifica el carrito y registra historial")
    void removerProductoInexistenteRegistraHistorial() {
      int tamanoAntes = carrito.getHistorialOperaciones().size();

      carrito.removerProducto(laptop);

      assertTrue(carrito.getItems().isEmpty());
      assertTrue(carrito.getHistorialOperaciones().size() > tamanoAntes);

      String ultimaOp =
          carrito.getHistorialOperaciones().get(carrito.getHistorialOperaciones().size() - 1);

      assertTrue(ultimaOp.contains("El producto no se encontro en el carrito"));
    }
  }

  // -------------------------------------------------------------------------
  // Actualizar cantidad
  // -------------------------------------------------------------------------

  @Nested
  @DisplayName("Actualizar cantidad")
  class ActualizarCantidad {

    @Test
    @DisplayName("actualiza cantidad de producto existente")
    void actualizarCantidadProductoExistente() {
      carrito.agregarProducto(laptop, 2);

      carrito.actualizarCantidad(laptop, 5);

      assertEquals(5, carrito.getItems().get(0).getCantidad());
      assertEquals(5, carrito.getCantidadTotalProductos());
    }

    @Test
    @DisplayName("lanza excepción al actualizar con cantidad cero")
    void actualizarCantidadCero() {
      carrito.agregarProducto(laptop, 2);

      assertThrows(IllegalArgumentException.class, () -> carrito.actualizarCantidad(laptop, 0));
    }

    @Test
    @DisplayName("lanza excepción al actualizar con cantidad negativa")
    void actualizarCantidadNegativa() {
      carrito.agregarProducto(laptop, 2);

      assertThrows(IllegalArgumentException.class, () -> carrito.actualizarCantidad(laptop, -1));
    }

    @Test
    @DisplayName("actualizar cantidad registra operación en historial")
    void actualizarCantidadRegistraHistorial() {
      carrito.agregarProducto(laptop, 2);

      carrito.actualizarCantidad(laptop, 4);

      assertTrue(
          carrito.getHistorialOperaciones().stream()
              .anyMatch(op -> op.contains("Se actualizo la cantidad de Laptop a 4")));
    }

    @Test
    @DisplayName("actualizar producto inexistente no modifica el carrito")
    void actualizarProductoInexistenteNoModificaCarrito() {
      carrito.agregarProducto(laptop, 2);

      Producto teclado = new Producto("P004", "Teclado", 80.0, true);

      carrito.actualizarCantidad(teclado, 5);

      assertEquals(1, carrito.getItems().size());
      assertEquals(2, carrito.getItems().get(0).getCantidad());
    }
  }

  // -------------------------------------------------------------------------
  // Vaciar carrito
  // -------------------------------------------------------------------------

  @Nested
  @DisplayName("Vaciar carrito")
  class VaciarCarrito {

    @Test
    @DisplayName("vaciar elimina todos los ítems")
    void vaciarEliminaTodosLosItems() {
      carrito.agregarProducto(laptop, 1);
      carrito.agregarProducto(mouse, 3);
      carrito.vaciarCarrito();

      assertTrue(carrito.getItems().isEmpty());
      assertEquals(0, carrito.getCantidadTotalProductos());
    }

    @Test
    @DisplayName("vaciar registra operación en historial")
    void vaciarRegistraHistorial() {
      carrito.vaciarCarrito();
      String ultimaOp =
          carrito.getHistorialOperaciones().get(carrito.getHistorialOperaciones().size() - 1);

      assertTrue(ultimaOp.contains("Se vacio el carrito"));
    }
  }

  // -------------------------------------------------------------------------
  // Cálculos (sin cargos externos)
  // -------------------------------------------------------------------------

  @Nested
  @DisplayName("Cálculo de totales sin cargos")
  class CalculosTotales {

    @Test
    @DisplayName("subtotal es cero para carrito vacío")
    void subtotalCarritoVacio() {
      assertEquals(0.0, carrito.calcularPrecioProductos());
    }

    @Test
    @DisplayName("subtotal correcto con un producto")
    void subtotalUnProducto() {
      carrito.agregarProducto(laptop, 2);
      assertEquals(2000.0, carrito.calcularPrecioProductos());
    }

    @Test
    @DisplayName("subtotal correcto con múltiples productos")
    void subtotalMultiplesProductos() {
      carrito.agregarProducto(laptop, 1);
      carrito.agregarProducto(mouse, 4);

      assertEquals(1100.0, carrito.calcularPrecioProductos(), 0.001);
    }

    @Test
    @DisplayName("total igual al subtotal cuando descuento e impuesto son cero")
    void totalIgualSubtotalSinCargos() {
      carrito.agregarProducto(laptop, 1);

      assertEquals(carrito.calcularPrecioProductos(), carrito.calcularPrecioTotal());
    }
  }

  // -------------------------------------------------------------------------
  // Resumen de compra
  // -------------------------------------------------------------------------

  @Nested
  @DisplayName("Resumen de compra")
  class ResumenCompra {

    @Test
    @DisplayName("resumen de carrito vacío muestra encabezado")
    void resumenCarritoVacioMuestraEncabezado() {
      String resumen = carrito.obtenerResumenCompra();

      assertTrue(resumen.contains("Resumen de productos:"));
    }

    @Test
    @DisplayName("resumen contiene datos de un producto")
    void resumenContieneDatosDeUnProducto() {
      carrito.agregarProducto(laptop, 2);

      String resumen = carrito.obtenerResumenCompra();

      assertAll(
          () -> assertTrue(resumen.contains("Producto: Laptop")),
          () -> assertTrue(resumen.contains("Cantidad: 2")),
          () -> assertTrue(resumen.contains("Precio: 1000.0")));
    }

    @Test
    @DisplayName("resumen contiene múltiples productos")
    void resumenContieneMultiplesProductos() {
      carrito.agregarProducto(laptop, 1);
      carrito.agregarProducto(mouse, 3);

      String resumen = carrito.obtenerResumenCompra();

      assertAll(
          () -> assertTrue(resumen.contains("Producto: Laptop")),
          () -> assertTrue(resumen.contains("Cantidad: 1")),
          () -> assertTrue(resumen.contains("Producto: Mouse")),
          () -> assertTrue(resumen.contains("Cantidad: 3")),
          () -> assertTrue(resumen.contains("Precio: 25.0")));
    }

    @Test
    @DisplayName("obtener resumen registra operación en historial")
    void resumenRegistraOperacionEnHistorial() {
      int tamanoAntes = carrito.getHistorialOperaciones().size();

      carrito.obtenerResumenCompra();

      assertTrue(carrito.getHistorialOperaciones().size() > tamanoAntes);

      String ultimaOperacion =
          carrito.getHistorialOperaciones().get(carrito.getHistorialOperaciones().size() - 1);

      assertTrue(ultimaOperacion.contains("Se obtuvo el resumen de productos"));
    }
  }

  // -------------------------------------------------------------------------
  // Casos límite
  // -------------------------------------------------------------------------

  @Nested
  @DisplayName("Casos límite")
  class CasosLimite {

    @Test
    @DisplayName("carrito con exactamente 1 producto calcula correctamente")
    void carritoConUnProducto() {
      carrito.agregarProducto(mouse, 1);

      assertAll(
          () -> assertEquals(1, carrito.getItems().size()),
          () -> assertEquals(25.0, carrito.calcularPrecioProductos(), 0.001),
          () -> assertEquals(25.0, carrito.calcularPrecioTotal()));
    }

    @Test
    @DisplayName("carrito con 100 productos distintos calcula subtotal correcto")
    void carritoConCienProductos() {
      double subtotalEsperado = 0.0;
      for (int i = 1; i <= 100; i++) {
        Producto p = new Producto("P" + i, "Producto " + i, i * 10.0, true);
        carrito.agregarProducto(p, 1);
        subtotalEsperado += i * 10.0;
      }

      assertEquals(100, carrito.getItems().size());
      assertEquals(subtotalEsperado, carrito.calcularPrecioProductos(), 0.001);
    }

    @Test
    @DisplayName("contieneProducto retorna false para producto no agregado")
    void contieneProductoNoAgregado() {
      assertFalse(carrito.contieneProducto(laptop));
    }
  }
}
