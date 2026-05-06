import pytest
from sources.descuentos import calcular_descuento_extras

# --- PASO 1: FASE RED (Pruebas Iniciales) ---

def test_descuento_tres_extras():
    """Prueba el límite inferior del primer tramo de descuento (10%)"""
    assert calcular_descuento_extras(3) == 10

def test_descuento_cinco_extras():
    """Prueba el límite inferior del segundo tramo de descuento (15%)"""
    assert calcular_descuento_extras(5) == 15

# --- PASO 4: VALIDACIÓN FINAL (Valores Límite y Robustez) ---
def test_limites_y_particiones():
    """
    Análisis de valores límite (Myers):
    Se prueban los valores justo antes y después de los cambios de estado.
    """
    assert calcular_descuento_extras(2) == 0   # Límite inferior (sin descuento)
    assert calcular_descuento_extras(3) == 10  # Frontera exacta 10%
    assert calcular_descuento_extras(4) == 10  # Dentro de la clase 10%
    assert calcular_descuento_extras(5) == 15  # Frontera exacta 15%
    assert calcular_descuento_extras(10) == 15 # Valor alejado dentro de la clase 15%

def test_entradas_invalidas():
    """
    Prueba de Robustez:
    Verifica que el sistema maneje correctamente datos erróneos.
    """
    with pytest.raises(ValueError):
        calcular_descuento_extras(-1)
    
    with pytest.raises(ValueError):
        calcular_descuento_extras("tres")

