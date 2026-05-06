# bank_account.py

class SaldoInsuficienteError(Exception):
    """Se lanza cuando se intenta retirar más del saldo disponible."""
    pass

class MontoInvalidoError(Exception):
    """Se lanza cuando el monto es cero o negativo."""
    pass

class BankAccount:
    def __init__(self, titular: str, saldo_inicial: float = 0.0):
        if saldo_inicial < 0:
            raise MontoInvalidoError("El saldo inicial no puede ser negativo.")
        self.titular = titular
        self._saldo = saldo_inicial

    @property
    def saldo(self) -> float:
        return self._saldo

    def depositar(self, monto: float) -> None:
        if monto <= 0:
            raise MontoInvalidoError(f"Monto inválido: {monto}. Debe ser positivo.")
        self._saldo += monto

    def retirar(self, monto: float) -> None:
        if monto <= 0:
            raise MontoInvalidoError(f"Monto inválido: {monto}. Debe ser positivo.")
        if monto > self._saldo:
            raise SaldoInsuficienteError(
                f"Saldo insuficiente: tiene S/.{self._saldo}, intenta retirar S/.{monto}"
            )
        self._saldo -= monto
