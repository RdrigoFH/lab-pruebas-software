import pytest
import atm


@pytest.fixture
def cuenta_de_banco():
    atm.inicializar_saldo(1000.0)
    return atm

# Pruebas Individuales
def test_saldo_inicial_correcto():
    # TC-01
    # Arrange
    saldo = 1000.0
    esperado = 1000.0

    # Act
    resultado = atm.inicializar_saldo(saldo)

    # Assert
    assert resultado == esperado


def test_deposito_valido(cuenta_de_banco):
    # TC-02
    # Arrange
    monto = 500.0
    esperado = 1500.0

    # Act
    atm.depositar(monto)

    # Assert
    assert atm.saldo == esperado


def test_retiro_valido(cuenta_de_banco):
    # TC-03
    # Arrange
    monto = 300.0
    esperado = 700.0

    # Act
    atm.retirar(monto)

    # Assert
    assert atm.saldo == esperado


def test_retiro_exacto_al_saldo_disponible(cuenta_de_banco):
    # TC-04
    # Arrange
    monto = 1000.0
    esperado = 0.0

    # Act
    atm.retirar(monto)

    # Assert
    assert atm.saldo == esperado


def test_retiro_mayor_al_saldo_lanza_error(cuenta_de_banco):
    # TC-05
    # Arrange
    monto = 1001.0
    mensaje_error = "Fondos insuficientes"

    # Act & Assert
    with pytest.raises(ValueError, match=mensaje_error):
        atm.retirar(monto)


def test_consulta_saldo_no_modifica_estado(cuenta_de_banco):
    # TC-12
    # Arrange
    saldo_antes = atm.saldo

    # Act
    atm.consultar_saldo()
    atm.consultar_saldo()
    atm.consultar_saldo()

    # Assert
    assert atm.saldo == saldo_antes


# Parametrizadas

@pytest.mark.parametrize("operacion, monto, mensaje_error", [
    # TC-06 al TC-08
    (atm.depositar, -200.0, "El importe debe ser mayor que cero"),
    (atm.depositar, 0.0, "El importe debe ser mayor que cero"),
    (atm.retirar, -50.0, "El importe debe ser mayor que cero"),
    # Caso adicional
    (atm.retirar, 0.0, "El importe debe ser mayor que cero"),
])
def test_operaciones_con_monto_invalido(cuenta_de_banco, operacion, monto, mensaje_error):
    # Arrange
    # operacion
    # monto
    # mensaje_error

    # Act & Assert
    with pytest.raises(ValueError, match=mensaje_error):
        operacion(monto)


@pytest.mark.parametrize("saldo, mensaje_error", [
    # TC-09
    (-500.0, "El saldo inicial no puede ser negativo"),

    # Casos adicionales
    (-1.0, "El saldo inicial no puede ser negativo"),
    (-1000.0, "El saldo inicial no puede ser negativo"),
])
def test_saldo_inicial_negativo_lanza_error(saldo, mensaje_error):
    # Arrange
    # saldo
    # mensaje_error

    # Act & Assert
    with pytest.raises(ValueError, match=mensaje_error):
        atm.inicializar_saldo(saldo)

# Saldo valido
@pytest.mark.parametrize("saldo, esperado", [
    (0.0, 0.0),
    (500.0, 500.0),
    (1000.0, 1000.0),
    (10000.0, 10000.0),
])
def test_saldos_iniciales_validos(saldo, esperado):
    # Arrange
    # saldo
    # esperado

    # Act
    resultado = atm.inicializar_saldo(saldo)

    # Assert
    assert resultado == esperado


@pytest.mark.parametrize("monto, esperado", [
    (100.0, 1100.0),
    (250.0, 1250.0),
    (500.0, 1500.0),
])
def test_depositos_validos_parametrizados(cuenta_de_banco, monto, esperado):
    # Arrange
    # monto
    # esperado

    # Act
    atm.depositar(monto)

    # Assert
    assert atm.saldo == esperado


@pytest.mark.parametrize("monto, esperado", [
    (100.0, 900.0),
    (300.0, 700.0),
    (500.0, 500.0),
    (1000.0, 0.0),
])
def test_retiros_validos_parametrizados(cuenta_de_banco, monto, esperado):
    # Arrange
    # monto
    # esperado

    # Act
    atm.retirar(monto)

    # Assert
    assert atm.saldo == esperado


# ============================================================
# PRUEBAS PARAMETRIZADAS ACUMULADAS
# ============================================================

@pytest.mark.parametrize("lista_depositos, esperado", [
    # TC-10
    ([100.0, 200.0, 300.0], 1600.0),

    # Casos adicionales
    ([500.0, 500.0], 2000.0),
    ([50.0, 50.0, 100.0], 1200.0),
])
def test_multiples_depositos_acumulados(cuenta_de_banco, lista_depositos, esperado):
    # Arrange
    # lista_depositos
    # esperado

    # Act
    for deposito in lista_depositos:
        atm.depositar(deposito)

    # Assert
    assert atm.saldo == esperado


@pytest.mark.parametrize("lista_retiros, esperado", [
    # TC-11
    ([100.0, 200.0, 300.0], 400.0),

    # Casos adicionales
    ([250.0, 250.0], 500.0),
    ([100.0, 100.0, 100.0], 700.0),
])
def test_multiples_retiros_acumulados(cuenta_de_banco, lista_retiros, esperado):
    # Arrange
    # lista_retiros
    # esperado

    # Act
    for retiro in lista_retiros:
        atm.retirar(retiro)

    # Assert
    assert atm.saldo == esperado



