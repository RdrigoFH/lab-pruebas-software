def clasificar_triangulo(lado1, lado2, lado3):
    """
    Esta función clasifica un triángulo según sus lados.

    Args:
        lado1: La longitud del primer lado.
        lado2: La longitud del segundo lado.
        lado3: La longitud del tercer lado.

    Returns:
        Un string que indica si el triángulo es escaleno, isósceles, equilátero o inválido.
    """

    # Verificar si el triángulo es válido
    if lado1 + lado2 <= lado3 or lado1 + lado3 <= lado2 or lado2 + lado3 <= lado1:
        return "Triángulo inválido"

    # Clasificar el triángulo
    if lado1 == lado2 == lado3:
        return "Triángulo equilátero"
    elif lado1 == lado2 or lado1 == lado3 or lado2 == lado3:
        return "Triángulo isósceles"
    else:
        return "Triángulo escaleno"


# Solicitar al usuario la longitud de los lados
lado1 = int(input("Ingrese la longitud del primer lado: "))
lado2 = int(input("Ingrese la longitud del segundo lado: "))
lado3 = int(input("Ingrese la longitud del tercer lado: "))

# Clasificar el triángulo y mostrar el resultado
clasificacion = clasificar_triangulo(lado1, lado2, lado3)
print(f"El triángulo es: {clasificacion}")
