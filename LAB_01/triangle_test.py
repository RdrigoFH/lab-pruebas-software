import sys

"""
Este programa lee tres números enteros que representan las longitudes de los lados
de un triángulo y determina si es equilátero, isósceles, escaleno o inválido.
"""


def clasificar_triangulo(lado1, lado2, lado3):
    """
    Clasifica un triángulo basado en las longitudes de sus lados.

    Args:
      lado1: Longitud del primer lado (entero).
      lado2: Longitud del segundo lado (entero).
      lado3: Longitud del tercer lado (entero).

    Returns:
      Una cadena de texto indicando el tipo de triángulo o si es inválido.
    """
    # Verificar que los lados sean positivos
    if lado1 <= 0 or lado2 <= 0 or lado3 <= 0:
        return "Triángulo inválido: Las longitudes de los lados deben ser positivas."

    # Verificar la desigualdad del triángulo: la suma de dos lados debe ser mayor que el tercero
    if (lado1 + lado2 <= lado3) or \
       (lado1 + lado3 <= lado2) or \
       (lado2 + lado3 <= lado1):
        return "Triángulo inválido: Las longitudes no cumplen la desigualdad triangular."

    # Clasificar el triángulo si es válido
    if lado1 == lado2 == lado3:
        return "El triángulo es equilátero."
    elif lado1 == lado2 or lado1 == lado3 or lado2 == lado3:
        return "El triángulo es isósceles."
    else:
        return "El triángulo es escaleno."


# --- Programa Principal ---
if __name__ == "__main__":
    print("Ingrese las longitudes de los tres lados del triángulo:")
    args = sys.argv[1:]
    a = []
    for arg in args:
        try:
            a.append(int(arg))
        except ValueError:
            print("Error: Por favor, ingrese solo números enteros para las longitudes de los lados.")


    try:
        # Leer las entradas del usuario y convertirla232s a entero
        s1 = a[0]
        s2 = a[1]
        s3 = a[2]
        
        # Llamar a la función para clasificar y obtener el resultado
        resultado = clasificar_triangulo(s1, s2, s3)

        # Imprimir el resultado
        print(resultado)

    except ValueError:
        # Manejar el caso en que la entrada no sea un número entero válido
        print("Error: Por favor, ingrese solo números enteros para las longitudes de los lados.")
