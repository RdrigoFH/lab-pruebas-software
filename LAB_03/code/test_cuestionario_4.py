def test_formato_nombre():
  assert formato_nombre("Jorge", "Mamani") == "Mamani, Jorge"

def formato_nombre(nombre, apellido):
  return apellido + ", " + nombre
