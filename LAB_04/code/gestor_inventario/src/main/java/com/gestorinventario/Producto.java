package com.gestorinventario;

public class Producto {
    private String codigo;
    private String nombre;
    private double precio;
    private int cantidad;

    public Producto(String codigo, String nombre, String precio, String cantidad) {
        setCodigo(codigo);
        setNombre(nombre);
        setPrecio(precio);
        setCantidad(cantidad);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo no puede estar vacio");
        }

        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        try {
            double precioNumerico = Double.parseDouble(precio);

            if (precioNumerico <= 0) {
                throw new IllegalArgumentException("El precio debe ser positivo");
            }

            this.precio = precioNumerico;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El precio debe ser un valor numerico válido");
        }
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        try {
            int cantidadNumerica = Integer.parseInt(cantidad);

            if (cantidadNumerica < 0) {
                throw new IllegalArgumentException("La cantidad no puede ser negativa");
            }

            this.cantidad = cantidadNumerica;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La cantidad debe ser un valor numerico válido");
        }
    }

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