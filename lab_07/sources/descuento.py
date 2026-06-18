import sys


def calcular_descuento(edad: int, es_miembro: bool, monto_compra: float) -> float:
    """
    Calcula el precio final aplicando reglas de negocio de descuentos.
    Decisión: (edad >= 65 OR es_miembro) AND (monto_compra >= 1000)
    """
    # Flujo de datos: Definición inicial (Def 1)
    descuento = 0.0

    # Evaluación de la decisión compuesta
    if (edad >= 65 or es_miembro) and (monto_compra >= 1000):
        # Flujo de datos: Redefinición si la regla se cumple (Def 2)
        descuento = 0.15
    else:
        # Flujo de datos: Redefinición alternativa (Def 3)
        descuento = 0.00

    # Flujo de datos: Uso de la variable 'descuento' (Uso)
    precio_final = monto_compra - (monto_compra * descuento)

    return precio_final


if __name__ == "__main__":  # pragma: no cover
    # Obtenemos los argumentos pasados por terminal (omitiendo el nombre del script en el índice 0)
    args = sys.argv[1:]

    # Valores por defecto
    edad_val = None
    es_miembro_val = False
    monto_val = None

    try:
        # Extracción manual de argumentos y sus valores
        if "--edad" in args:
            idx = args.index("--edad")
            edad_val = int(args[idx + 1])

        if "--miembro" in args:
            es_miembro_val = True

        if "--monto" in args:
            idx = args.index("--monto")
            monto_val = float(args[idx + 1])

        # Validación de parámetros obligatorios
        if edad_val is None or monto_val is None:
            print("Error: Los parámetros --edad y --monto son obligatorios.")
            print(
                "Uso correcto: python3 descuento.py --edad <entero> [--miembro] --monto <decimal>")
            sys.exit(1)

        # Ejecución de la función principal
        resultado = calcular_descuento(edad_val, es_miembro_val, monto_val)
        print(f"El precio final a pagar es: S/ {resultado:.2f}")

    except (ValueError, IndexError):
        # Captura errores si falta el valor después de la bandera o si el tipo de dato es incorrecto
        print("Error: Formato de argumentos inválido.")
        print(
            "Uso correcto: python3 descuento.py --edad <entero> [--miembro] --monto <decimal>")
        sys.exit(1)
