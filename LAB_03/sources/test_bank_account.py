# test_bank_account.py
import pytest
from bank_account import BankAccount, SaldoInsuficienteError, MontoInvalidoError

# ─── Fixture ────────────────────────────────────────────────────────
@pytest.fixture
def cuenta():
    """Fixture: retorna una BankAccount con S/.1000 de saldo inicial."""
    return BankAccount(titular="Luis Quispe", saldo_inicial=1000.0)

# ─── Pruebas: saldo y depósito ──────────────────────────────────────
def test_saldo_inicial(cuenta):
    assert cuenta.saldo == 1000.0

def test_deposito_incrementa_saldo(cuenta):
    cuenta.depositar(500.0)
    assert cuenta.saldo == 1500.0

def test_retiro_decrementa_saldo(cuenta):
    cuenta.retirar(200.0)
    assert cuenta.saldo == 800.0

# ─── Pruebas: excepciones ───────────────────────────────────────────
def test_retiro_excede_saldo_lanza_excepcion(cuenta):
    with pytest.raises(SaldoInsuficienteError):
        cuenta.retirar(2000.0)

def test_deposito_monto_negativo_lanza_excepcion(cuenta):
    with pytest.raises(MontoInvalidoError):
        cuenta.depositar(-100.0)

def test_retiro_monto_cero_lanza_excepcion(cuenta):
    with pytest.raises(MontoInvalidoError):
        cuenta.retirar(0)

def test_cuenta_saldo_inicial_negativo_lanza_excepcion():
    with pytest.raises(MontoInvalidoError):
        BankAccount("Error", saldo_inicial=-500)

# ─── Prueba parametrizada: múltiples retiros ────────────────────────
@pytest.mark.parametrize("retiro, saldo_esperado", [
    (100.0,  900.0),
    (500.0,  500.0),
    (999.99,   0.01),
    (1000.0,   0.0),
])
def test_retiros_validos(cuenta, retiro, saldo_esperado):
    cuenta.retirar(retiro)
    assert abs(cuenta.saldo - saldo_esperado) < 0.001
