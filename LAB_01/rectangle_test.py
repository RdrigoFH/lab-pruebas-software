import sys
import math

"""
El programa calcula el área del rectángulo (Área = base * altura) e imprimir el resultado

El presente script solicita al usuario ingresar la base y la altura de un rectángulo como números enteros o decimales .

Mostrando claramente cuál es la base, la altura y el área calculada.
"""

def calculate_area_rectangle(base, height):
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
        try:
            n = float(arg)
            if n >= 0:
                a.append(n)
            else:
                raise ValueError("Se deben ingresar valores positivos")

        except ValueError:
            print("Error: por favor ingrese solo numeros positivos, sin caracteres adicionales, separados por espacio")
            sys.exit(1)
    
    if a:
        try:
            if len(a) < 2:
                raise ValueError("Se requieren al menos 2 valores")
            
            elif len(a) > 2:
                print("Mas de dos parametro ingresado, solo se consideraran los dos primeros para base y altura respectivamente")

            b = a[0]
            h = a[1]


        except ValueError as e:
            print(e)
            sys.exit(1)

    else:
        try:
            b = float(input("Ingrese la base del rectangul: "))
            h = float(input("Ingrese la altura del rectangulo: "))
            if b < 0 or h < 0:
                raise ValueError("Se deben ingresar valores positivos")

        except ValueError:
            print("Ingrese solo numeros enteros o decimales, no se aceptan caracteres o letras")

    ans = calculate_area_rectangle(b, h)

    if math.isinf(ans) or math.isnan(ans) or abs(ans) < 1e-308:
        print("Error: resultado fuera de rango")
        sys.exit(1)
    print(f"La base del rectangulo es: {b}")
    print(f"La altura del rectangulo es: {h}")
    print(f"El area del rectangulo de esas dimensiones es: {ans}")

