import sys
import math

MAX_VALUE = 1e+308
MIN_VALUE = -1e-308


"""
Crea un programa que primero solicite al usuario cuántos números desea ingresar. Luego, debe pedirle que ingrese cada uno de esos números enteros. Finalmente, el programa debe recorrer la lista de números ingresados e imprimir cada número junto con un mensaje indicando si es "par" o "impar".

Objetivo de prueba: Comprobar que el programa maneja correctamente la cantidad de números especificada, lee los números, e identifica y etiqueta correctamente cada número como par o impar (incluyendo el número cero y números negativos si se consideran).

"""

def is_even_or_odd(n):
    """
    Funcion para determinar si un número es par o impar

    Args:
        n -> Un numero entero

    Returns:
        "par" si el número es par, "impar" si el número es impar
    """
    if n % 2 == 0:
        return "par"
    else:
        return "impar"
    
# --- Programa Principal ---
if __name__ == "__main__":
    try:
        length = int(input("Cuantos numeros desea ingresar? "))
        if length <= 0:
            print("Error: la cantidad debe ser un entero positivo.")
            sys.exit(1)
    except ValueError:
        print("Error: ingrese un numero entero valido.")
        sys.exit(1)
    
    numbers = []
    for i in range(length):
        while True:
            try:
                num = int(input(f"Ingrese el numero {i+1}: \n"))
                numbers.append(num)
                break
            except ValueError:
                print("Error: ingrese un numero entero valido.")
                sys.exit(1)

    for num in numbers:
        print(f"{num} es {is_even_or_odd(num)}")



