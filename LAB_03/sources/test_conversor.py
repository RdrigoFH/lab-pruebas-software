# test_conversor.py
import pytest
from conversor import celsius_a_fahrenheit, fahrenheit_a_celsius

# ─── Pruebas para celsius_a_fahrenheit ─────────────────────────────

def test_celsius_a_fahrenheit_punto_congelacion():
    # Arrange
    celsius = 0
    # Act
    resultado = celsius_a_fahrenheit(celsius)
    # Assert
    assert resultado == 32.0, f"Esperado 32.0 pero obtuvo {resultado}"

def test_celsius_a_fahrenheit_punto_ebullicion():
    # Arrange
    celsius = 100
    # Act
    resultado = celsius_a_fahrenheit(celsius)
    # Assert
    assert resultado == 212.0

def test_celsius_a_fahrenheit_temperatura_negativa():
    assert celsius_a_fahrenheit(-40) == -40.0  # punto de equivalencia

# ─── Pruebas parametrizadas ─────────────────────────────────────────

@pytest.mark.parametrize("celsius, fahrenheit", [
    (0,    32.0),
    (100,  212.0),
    (-40,  -40.0),
    (37,   98.6),   # temperatura corporal normal
    (-273.15, -459.67),  # cero absoluto
])
def test_celsius_a_fahrenheit_parametrizado(celsius, fahrenheit):
    resultado = celsius_a_fahrenheit(celsius)
    assert abs(resultado - fahrenheit) < 0.01, (
        f"celsius={celsius}: esperado {fahrenheit}, obtuvo {resultado}"
    )
