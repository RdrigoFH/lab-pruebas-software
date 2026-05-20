package com.lab04;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Representa el carrito de compras de un cliente en la tienda en línea.
 *
 * <p>Permite agregar y remover productos, calcular totales aplicando descuentos e impuestos
 * a través de un {@link ServicioPrecio}, y obtener un resumen detallado de la compra.
 * Mantiene un historial inmutable de todas las operaciones realizadas.
 *
 * <p>Invariantes:
 * <ul>
 *   <li>No se admiten productos nulos ni no disponibles.</li>
 *   <li>Las cantidades siempre son positivas.</li>
 *   <li>No existen ítems duplicados: agregar un producto ya existente suma su cantidad.</li>
 * </ul>
 */
public class CarritoCompra {

  private final List<ItemCarrito> items;
  private final ServicioPrecio servicioPrecio;
  private final List<String> historialOperaciones;

  /**
   * Construye un carrito de compras con el servicio de precios indicado.
   *
   * @param servicioPrecio servicio externo de cálculo de precios, no puede ser nulo
   * @throws IllegalArgumentException si {@code servicioPrecio} es nulo
   */
  public CarritoCompra(ServicioPrecio servicioPrecio) {
    if (servicioPrecio == null) {
      throw new IllegalArgumentException("El servicio de precios no puede ser nulo");
    }
    this.items = new ArrayList<>();
    this.servicioPrecio = servicioPrecio;
    this.historialOperaciones = new ArrayList<>();
    registrarOperacion("Carrito creado");
  }

  /**
   * Retorna una vista no modificable de los ítems del carrito.
   *
   * @return lista inmutable de ítems
   */
  public List<ItemCarrito> getItems() {
    return Collections.unmodifiableList(items);
  }

  /**
   * Retorna una vista no modificable del historial de operaciones.
   *
   * @return lista inmutable de entradas de historial con timestamp
   */
  public List<String> getHistorialOperaciones() {
    return Collections.unmodifiableList(historialOperaciones);
  }

  /**
   * Agrega un producto al carrito con la cantidad especificada.
   *
   * <p>Si el producto ya existe en el carrito, su cantidad se incrementa en lugar de
   * crear un ítem duplicado.
   *
   * @param producto producto a agregar, no puede ser nulo ni no disponible
   * @param cantidad cantidad a agregar, debe ser positiva
   * @throws IllegalArgumentException si el producto es nulo o la cantidad no es positiva
   * @throws IllegalStateException si el producto no está disponible
   */
  public void agregarProducto(Producto producto, int cantidad) {
    if (producto == null) {
      throw new IllegalArgumentException("El producto no puede ser nulo");
    }
    if (cantidad <= 0) {
      throw new IllegalArgumentException("La cantidad debe ser positiva");
    }
    if (!producto.isDisponible()) {
      throw new IllegalStateException(
          "No se puede agregar un producto no disponible: " + producto.getNombre());
    }

    Optional<ItemCarrito> itemExistente = items.stream()
        .filter(item -> item.getProducto().equals(producto))
        .findFirst();

    if (itemExistente.isPresent()) {
      ItemCarrito item = itemExistente.get();
      int nuevaCantidad = item.getCantidad() + cantidad;
      item.setCantidad(nuevaCantidad);
      registrarOperacion(String.format(
          "Cantidad actualizada para %s: +%d (nueva cantidad: %d)",
          producto.getNombre(), cantidad, nuevaCantidad));
    } else {
      items.add(new ItemCarrito(producto, cantidad));
      registrarOperacion(String.format(
          "Producto agregado: %s x%d", producto.getNombre(), cantidad));
    }
  }

  /**
   * Remueve una cantidad del producto indicado del carrito.
   *
   * <p>Si la cantidad a remover es mayor o igual a la existente, el ítem se elimina
   * completamente del carrito.
   *
   * @param producto producto a remover, no puede ser nulo
   * @param cantidad cantidad a remover, debe ser positiva
   * @throws IllegalArgumentException si el producto es nulo o la cantidad no es positiva
   * @throws IllegalStateException si el producto no existe en el carrito
   */
  public void removerProducto(Producto producto, int cantidad) {
    if (producto == null) {
      throw new IllegalArgumentException("El producto no puede ser nulo");
    }
    if (cantidad <= 0) {
      throw new IllegalArgumentException("La cantidad a remover debe ser positiva");
    }

    Optional<ItemCarrito> itemExistente = items.stream()
        .filter(item -> item.getProducto().equals(producto))
        .findFirst();

    if (itemExistente.isEmpty()) {
      throw new IllegalStateException(
          "El producto no existe en el carrito: " + producto.getNombre());
    }

    ItemCarrito item = itemExistente.get();
    int nuevaCantidad = item.getCantidad() - cantidad;

    if (nuevaCantidad <= 0) {
      items.remove(item);
      registrarOperacion(
          String.format("Producto removido completamente: %s", producto.getNombre()));
    } else {
      item.setCantidad(nuevaCantidad);
      registrarOperacion(String.format(
          "Cantidad reducida para %s: -%d (nueva cantidad: %d)",
          producto.getNombre(), cantidad, nuevaCantidad));
    }
  }

  /** Elimina todos los ítems del carrito y registra la operación en el historial. */
  public void vaciarCarrito() {
    items.clear();
    registrarOperacion("Carrito vaciado completamente");
  }

  /**
   * Calcula la suma de los subtotales de todos los ítems, sin descuentos ni impuestos.
   *
   * @return subtotal del carrito; {@code 0.0} si está vacío
   */
  public double calcularSubtotal() {
    return items.stream()
        .mapToDouble(ItemCarrito::getSubtotal)
        .sum();
  }

  /**
   * Calcula el descuento a aplicar sobre el subtotal actual del carrito.
   *
   * @return monto de descuento según {@link ServicioPrecio}
   */
  public double calcularDescuento() {
    return servicioPrecio.calcularDescuento(calcularSubtotal());
  }

  /**
   * Calcula el impuesto a aplicar sobre el subtotal actual del carrito.
   *
   * @return monto de impuesto según {@link ServicioPrecio}
   */
  public double calcularImpuesto() {
    return servicioPrecio.calcularImpuesto(calcularSubtotal());
  }

  /**
   * Calcula el total a pagar aplicando descuentos e impuestos sobre el subtotal.
   *
   * <p>Fórmula: {@code total = (subtotal - descuento) + impuesto}
   *
   * @return total final del carrito
   */
  public double calcularTotal() {
    double subtotal = calcularSubtotal();
    double descuento = servicioPrecio.calcularDescuento(subtotal);
    double impuesto = servicioPrecio.calcularImpuesto(subtotal);
    double total = (subtotal - descuento) + impuesto;

    registrarOperacion(String.format(
        "Total calculado: subtotal=%.2f, descuento=%.2f, impuesto=%.2f, total=%.2f",
        subtotal, descuento, impuesto, total));
    return total;
  }

  /**
   * Genera un resumen legible de la compra con el desglose de precios.
   *
   * <p>Llama a {@link ServicioPrecio} una única vez por concepto para evitar
   * efectos secundarios no deseados ni entradas espurias en el historial.
   *
   * @return cadena con el resumen; mensaje especial si el carrito está vacío
   */
  public String obtenerResumenCompra() {
    if (items.isEmpty()) {
      return "El carrito está vacío";
    }

    double subtotal = calcularSubtotal();
    double descuento = servicioPrecio.calcularDescuento(subtotal);
    double impuesto = servicioPrecio.calcularImpuesto(subtotal);
    double total = (subtotal - descuento) + impuesto;

    StringBuilder sb = new StringBuilder();
    sb.append("=== RESUMEN DE COMPRA ===\n");
    sb.append("Productos:\n");

    for (ItemCarrito item : items) {
      sb.append(String.format(
          "  - %s: %d x $%.2f = $%.2f\n",
          item.getProducto().getNombre(),
          item.getCantidad(),
          item.getProducto().getPrecio(),
          item.getSubtotal()));
    }

    sb.append(String.format("%nSubtotal: $%.2f%n", subtotal));
    sb.append(String.format("Descuento: -$%.2f%n", descuento));
    sb.append(String.format("Impuesto: +$%.2f%n", impuesto));
    sb.append(String.format("TOTAL: $%.2f%n", total));

    return sb.toString();
  }

  /**
   * Retorna la cantidad total de unidades en el carrito (suma de cantidades de todos los ítems).
   *
   * @return cantidad total de productos
   */
  public int getCantidadTotalProductos() {
    return items.stream()
        .mapToInt(ItemCarrito::getCantidad)
        .sum();
  }

  /**
   * Verifica si el carrito contiene al menos una unidad del producto dado.
   *
   * @param producto producto a buscar
   * @return {@code true} si el producto está en el carrito
   */
  public boolean contieneProducto(Producto producto) {
    return items.stream()
        .anyMatch(item -> item.getProducto().equals(producto));
  }

  /**
   * Registra una entrada en el historial de operaciones con timestamp.
   *
   * @param operacion descripción de la operación realizada
   */
  private void registrarOperacion(String operacion) {
    historialOperaciones.add(
        String.format("[%s] %s", LocalDateTime.now(), operacion));
  }
}