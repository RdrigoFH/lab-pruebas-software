import pytest
from descuento import calcular_descuento

# =========================================================
# 1 & 2. STATEMENT TESTING y BRANCH TESTING
# =========================================================
# Para lograr un 100% en ambas métricas, solo necesitamos
# ejecutar cada línea de código (Statement) y transitar
# por las aristas True y False del 'if' (Branch).
# Lo logramos con solo 2 casos de prueba.


def test_statement_branch_camino_verdadero():
    """Ejecuta el bloque del IF (15% de descuento)"""
    assert calcular_descuento(
        edad=70, es_miembro=False, monto_compra=1500) == 1275.0


def test_statement_branch_camino_falso():
    """Ejecuta el bloque del ELSE (Sin descuento)"""
    assert calcular_descuento(
        edad=30, es_miembro=False, monto_compra=500) == 500.0


# =========================================================
# 3. BRANCH CONDITIONS COMBINATION TESTING
# =========================================================
# Decisión: (C1 OR C2) AND C3
# C1: edad >= 65
# C2: es_miembro == True
# C3: monto_compra >= 1000
# Requisito: Probar las 8 (2^3) combinaciones de V y F.

@pytest.mark.parametrize(
    "edad, miembro, monto, esperado, tabla_verdad",
    [
        # 1. C1=V, C2=V, C3=V -> Resultado: True (Aplica Descuento)
        (70, True,  1500, 1275.0, "V-V-V"),

        # 2. C1=V, C2=V, C3=F -> Resultado: False (No Aplica)
        (70, True,  500,  500.0,  "V-V-F"),

        # 3. C1=V, C2=F, C3=V -> Resultado: True (Aplica Descuento)
        (70, False, 1500, 1275.0, "V-F-V"),

        # 4. C1=V, C2=F, C3=F -> Resultado: False (No Aplica)
        (70, False, 500,  500.0,  "V-F-F"),

        # 5. C1=F, C2=V, C3=V -> Resultado: True (Aplica Descuento)
        (30, True,  1500, 1275.0, "F-V-V"),

        # 6. C1=F, C2=V, C3=F -> Resultado: False (No Aplica)
        (30, True,  500,  500.0,  "F-V-F"),

        # 7. C1=F, C2=F, C3=V -> Resultado: False (No Aplica)
        (30, False, 1500, 1500.0, "F-F-V"),

        # 8. C1=F, C2=F, C3=F -> Resultado: False (No Aplica)
        (30, False, 500,  500.0,  "F-F-F"),
    ]
)
def test_branch_conditions_combination(edad, miembro, monto, esperado, tabla_verdad):
    """
    Evalúa la matriz completa exhaustiva de las condiciones lógicas.
    """
    assert calcular_descuento(edad, miembro, monto) == esperado
