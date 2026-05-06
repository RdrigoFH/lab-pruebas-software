import sys

"""
Crea un programa que primero solicite al usuario cuántos números desea ingresar. Luego, debe pedirle que ingrese cada uno de esos números enteros. Finalmente, el programa debe recorrer la lista de números ingresados e imprimir cada número junto con un mensaje indicando si es "par" o "impar".

Objetivo de prueba: Comprobar que el programa maneja correctamente la cantidad de números especificada, lee los números, e identifica y etiqueta correctamente cada número como par o impar (incluyendo el número cero y números negativos si se consideran).

"""

def is_even_or_odd(n):
    """
    Funcion para determinar si un numero es par o impar

    Args:
        n -> Un numero entero

    Returns:
        "par" si el numero es par, "impar" si el numero es impar
    """
    if n % 2 == 0:
        return "par"
    else:
        return "impar"
    
# --- Programa Principal ---
if __name__ == "__main__":
    length = int(input("Cuantos numeros desea ingresar? "))
    numbers = []
    for i in range(length):
        num = int(input(f"Ingrese el numero {i+1}: "))
        numbers.append(num)

    for num in numbers:
        print(f"{num} es {is_even_or_odd(num)}")