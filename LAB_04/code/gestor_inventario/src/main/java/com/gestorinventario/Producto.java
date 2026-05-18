package com.gestorinventario;

public class Producto {
    public String codigo;
    public String nombre;
    public double precio;
    public int cantidad;

    public void agregarStock(int cantidad) {
        this.cantidad += cantidad;
    }

    public void extraerStock(int cantidad) {
        this.cantidad -= cantidad;
    }

    public int consultarStock() {
        return cantidad;
    }

    public double obtenerValorTotal() {
        return precio * cantidad;
    }
}