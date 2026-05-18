package main.java.com.lab04.Ejercicio2;

public class Producto {

    private int id;
    private String nombre;
    private double precio;
    private boolean disponibilidad;

    public Producto(int id, String nombre, double precio, boolean disponibilidad) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.disponibilidad = disponibilidad;
    }
}