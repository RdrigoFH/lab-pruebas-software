# conversor.py
# Módulo de conversión de temperaturas

def celsius_a_fahrenheit(celsius):
    """Convierte grados Celsius a Fahrenheit.
    Fórmula: F = (C * 9/5) + 32
    """
    return (celsius * 9 / 5) + 32

def fahrenheit_a_celsius(fahrenheit):
    """Convierte grados Fahrenheit a Celsius.
    Fórmula: C = (F - 32) * 5/9
    """
    return (fahrenheit - 32) * 5 / 9
