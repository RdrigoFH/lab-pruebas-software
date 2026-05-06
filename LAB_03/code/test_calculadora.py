# Para este ejercicio se utilizo IA para generar comentarios ordenados y claros
# Modelo utilizado: gemini 3.1 flash

import pytest
from calculadora import sumar, restar, multiplicar, dividir


# ============================================================
# 1. CASOS VÁLIDOS
# ============================================================

@pytest.mark.parametrize("funcion, numeros, esperado", [
    # Sumar
    (sumar, (5, 3), 8),
    (sumar, (5, 3, 2), 10),
    (sumar, (-1, 1, 0, 4), 4),
    (sumar, (2.5, 1.5, 1.0), 5.0),
    (sumar, (100, 200, 300), 600),
    # Restar
    (restar, (10, 3), 7),
    (restar, (10, 3, 2), 5),
    (restar, (0, 5, 5), -10),
    (restar, (-5, -3, -2), 0),
    # Multiplicar
    (multiplicar, (2, 3), 6),
    (multiplicar, (2, 3, 4), 24),
    (multiplicar, (-1, 5, 2), -10),
    (multiplicar, (2.5, 2), 5.0),     
    # Dividir
    (dividir, (10, 2), 5),
    (dividir, (100, 2, 5), 10),
    (dividir, (7, 2), 3.5),
    (dividir, (0, 5, 2), 0),
])
def test_operaciones_validas(funcion, numeros, esperado):
    # ---------- ARRANGE ----------
    # Los datos ya están preparados por la parametrización

    # ---------- ACT ----------
    resultado = funcion(*numeros)

    # ---------- ASSERT ----------
    assert resultado == esperado


# ============================================================
# 2. ERROR POR FALTA DE ARGUMENTOS (menos de 2)
# ============================================================

@pytest.mark.parametrize("funcion, numeros, mensaje_error", [
    (sumar, (), ValueError),
    (sumar, (5,), ValueError),
    (restar, (), ValueError),
    (restar, (5,), ValueError),
    (multiplicar, (1,), ValueError),    
    (multiplicar, (), ValueError),
    (dividir, (), ValueError),
    (dividir, (10,), ValueError),
])
def test_operaciones_menos_de_dos_argumentos(funcion, numeros, mensaje_error):
    # ---------- ARRANGE ----------
    # Nada que preparar, la parametrización trae todo

    # ---------- ACT & ASSERT ----------
    with pytest.raises(mensaje_error, match="Se requieren al menos dos argumentos"):
        funcion(*numeros)


# ============================================================
# 3. ERROR POR TIPOS INVÁLIDOS (strings, booleanos, None, listas)
# ============================================================

@pytest.mark.parametrize("funcion, numeros, tipo_error", [
    # Un string
    (sumar, ("x", 5), TypeError),
    (restar, (5, "y"), TypeError),
    (multiplicar, (2, "z"), TypeError),
    (dividir, ("a", 5), TypeError),
    # Booleanos (True/False)
    (sumar, (True, 5), TypeError),
    (restar, (False, 10), TypeError),
    (multiplicar, (10, True), TypeError),
    (dividir, (100, True), TypeError),
    # None
    (sumar, (None, 5), TypeError),
    (restar, (5, None), TypeError),
    # Listas u otros objetos
    (multiplicar, ([1, 2], 3), TypeError),
    (dividir, (10, {"a": 1}), TypeError),
    # Todos inválidos
    (sumar, ("a", "b"), TypeError),
    (restar, (True, False), TypeError),
])
def test_operaciones_tipos_invalidos(funcion, numeros, tipo_error):
    # ---------- ARRANGE ----------
    # Nada que preparar

    # ---------- ACT & ASSERT ----------
    with pytest.raises(tipo_error, match="no es un numero"):
        funcion(*numeros)


# ============================================================
# 4. DIVISIÓN POR CERO
# ============================================================

def test_dividir_por_cero_lanza_valueerror():
    # ---------- ARRANGE ----------
    # Preparo un escenario claro: varios números con un cero en medio
    numeros = (10, 2, 0, 4)
    mensaje_esperado = "No se puede dividir por cero"

    # ---------- ACT & ASSERT ----------
    with pytest.raises(ValueError, match=mensaje_esperado):
        dividir(*numeros)

def test_dividir_cero_entre_algo_es_valido():
    # ---------- ARRANGE ----------
    numeros = (0, 5, 2)
    esperado = 0

    # ---------- ACT ----------
    resultado = dividir(*numeros)

    # ---------- ASSERT ----------
    assert resultado == esperado