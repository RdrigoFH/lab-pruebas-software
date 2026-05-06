import pytest
from reembolso import calcular_reembolso

def test_cancelacion_mas_72_horas():
    assert calcular_reembolso(1000, 73, False) == 1000

def test_cancelacion_exactamente_72_horas():
    assert calcular_reembolso(1000, 72, False) == 500

def test_cancelacion_entre_24_y_72_horas():
    assert calcular_reembolso(1000, 48, False) == 500

def test_cancelacion_exactamente_24_horas():
    assert calcular_reembolso(1000, 24, False) == 500

def test_cancelacion_menos_24_horas():
    assert calcular_reembolso(1000, 23, False) == 0

def test_vip_menos_24_horas():
    assert calcular_reembolso(1000, 2, True) == 500

def test_vip_mas_72_horas():
    assert calcular_reembolso(1000, 73, True) == 1000

def test_monto_negativo():
    with pytest.raises(ValueError):
        calcular_reembolso(-100, 24, False)

def test_horas_negativas():
    with pytest.raises(ValueError):
        calcular_reembolso(1000, -5, False)
