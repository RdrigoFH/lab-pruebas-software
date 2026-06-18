def simple(a, b):
    if a > 0:
        if b == 0:
            print("sin valor")
        else:
            print(b)
            if a > 21:
                print(a)


# Bloque principal (Main)
if __name__ == "__main__":
    # Ejemplo de prueba interactiva
    try:
        val_a = float(input("Ingrese valor para A: "))
        val_b = float(input("Ingrese valor para B: "))

        print("--- Resultado ---")
        simple(val_a, val_b)

    except ValueError:
        print("Error: Por favor, ingrese solo números.")
