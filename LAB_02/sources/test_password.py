import unittest
from auth import validar_password

class TestPassword(unittest.TestCase):

    def test_escenario_valido(self):
        # Dado que ingreso una contraseña que cumple todos los requisitos
        # Cuando la contraseña es "Secure123"
        # Entonces el resultado debe ser True
        self.assertTrue(validar_password("Secure123"))

    def test_escenario_longitud_insuficiente(self):
        # Dado que ingreso una contraseña muy corta
        # Cuando la contraseña es "123"
        # Entonces el resultado debe ser False
        self.assertFalse(validar_password("123"))

    def test_escenario_sin_numeros(self):
        # Dado que ingreso una contraseña sin dígitos
        # Cuando la contraseña es "SoloLetras"
        # Entonces el resultado debe ser False
        self.assertFalse(validar_password("SoloLetras"))

    def test_valor_limite_8_caracteres(self):
        # Prueba de valor límite: exactamente 8 caracteres con número 
        self.assertTrue(validar_password("Passw0rd"))

unittest.main()