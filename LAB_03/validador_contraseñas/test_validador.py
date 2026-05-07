from validador import validar_contrasena


# =========================
# CASOS ACEPTADOS
# =========================

def test_contrasena_valida():

    # Arrange
    contrasena = "Segura#1"

    # Act
    resultado = validar_contrasena(contrasena)

    # Assert
    assert resultado["valida"] == True
    assert resultado["errores"] == []


def test_exactamente_8_caracteres_validos():

    # Arrange
    contrasena = "aB1!cDe2"

    # Act
    resultado = validar_contrasena(contrasena)

    # Assert
    assert resultado["valida"] == True


def test_contrasena_compleja():

    # Arrange
    contrasena = "Python#123"

    # Act
    resultado = validar_contrasena(contrasena)

    # Assert
    assert resultado["valida"] == True


# =========================
# CASOS DE ERROR
# =========================

def test_contrasena_muy_corta():

    # Arrange
    contrasena = "Ab1!"

    # Act
    resultado = validar_contrasena(contrasena)

    # Assert
    assert resultado["valida"] == False
    assert "Debe tener al menos 8 caracteres" in resultado["errores"]


def test_sin_mayuscula():

    # Arrange
    contrasena = "segura#1"

    # Act
    resultado = validar_contrasena(contrasena)

    # Assert
    assert resultado["valida"] == False
    assert "Debe contener al menos una letra mayúscula" in resultado["errores"]


def test_sin_minuscula():

    # Arrange
    contrasena = "SEGURA#1"

    # Act
    resultado = validar_contrasena(contrasena)

    # Assert
    assert resultado["valida"] == False
    assert "Debe contener al menos una letra minúscula" in resultado["errores"]


def test_sin_numero():

    # Arrange
    contrasena = "Segura##"

    # Act
    resultado = validar_contrasena(contrasena)

    # Assert
    assert resultado["valida"] == False
    assert "Debe contener al menos un número" in resultado["errores"]


def test_sin_caracter_especial():

    # Arrange
    contrasena = "Segura12"

    # Act
    resultado = validar_contrasena(contrasena)

    # Assert
    assert resultado["valida"] == False
    assert "Debe contener al menos un carácter especial" in resultado["errores"]


def test_contrasena_vacia():

    # Arrange
    contrasena = ""

    # Act
    resultado = validar_contrasena(contrasena)

    # Assert
    assert resultado["valida"] == False
    assert len(resultado["errores"]) == 5


def test_contrasena_none():

    # Arrange
    contrasena = None

    # Act
    resultado = validar_contrasena(contrasena)

    # Assert
    assert resultado["valida"] == False
    assert len(resultado["errores"]) == 5