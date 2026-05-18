package com.gestorinventario;

public class Producto {
    public String codigo;
    public String nombre;
    public double precio;
    public int cantidad;

    public void agregarStock(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad a agregar no puede ser negativa");
        }

        this.cantidad += cantidad;
    }

    public void extraerStock(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad a extraer no puede ser negativa");
        }

        if (cantidad > this.cantidad) {
            throw new IllegalArgumentException("El stock no puede quedar en negativo");
        }

        this.cantidad -= cantidad;
    }

    public int consultarStock() {
        return cantidad;
    }

    public double obtenerValorTotal() {
        return precio * cantidad;
    }

    public boolean codigoValido() {
        return codigo != null && !codigo.trim().isEmpty();
    }

    public boolean precioValido() {
        return precio > 0;
    }

    public boolean cantidadValida() {
        return cantidad >= 0;
    }
}