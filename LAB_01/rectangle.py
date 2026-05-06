import sys

"""
El programa calcula el área del rectángulo (Área = base * altura) e imprimir el resultado

El presente script solicita al usuario ingresar la base y la altura de un rectángulo como números enteros o decimales .

Mostrando claramente cuál es la base, la altura y el área calculada.
"""

def caluculate_area_rectangle(base, height):
    """
    Calculo del area usando la base y la altura

    Args:
        base -> La base del rectangulo
        height -> La altura del rectangulo

    
    Returns:
        un numero, entero o decimal producto de base y height, es el area del rectangulo
    """

    return base * height


# --- Programa Principal ---
if __name__ == "__main__":
    args = sys.argv[1:]
    a = []
    for arg in args:
        a.append(float(arg))
    
    if args:
        print(f"Base: {a[0]}")
        print(f"Altura: {a[1]}")
        ans = caluculate_area_rectangle(a[0], a[1])
        print(f"El area del rectangulo de esas dimensiones es: {ans}")
    else:
        b = float(input("Ingrese la base del triangulo"))
        h = float(input("Ingrese la altura del triangulo"))
        ans = caluculate_area_rectangle(b, h)
        print(f"El area del rectangulo de esas dimensiones es: {ans}")







