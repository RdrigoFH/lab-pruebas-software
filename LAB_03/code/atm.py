SALDO_INICIAL = 1000.0
saldo = 0.0


def inicializar_saldo(saldo_inicial=SALDO_INICIAL):
    global saldo

    if saldo_inicial < 0:
        raise ValueError(f"El saldo inicial no puede ser negativo")

    saldo = saldo_inicial
    return saldo

def principal():
    inicializar_saldo(SALDO_INICIAL)
    opcion = 0

    while opcion != 4:
        imprimir_menu()

        try:
            opcion = leer_opcion_entera()
            procesar_opcion(opcion)
        except ValueError as error:
            print(f"Error: {error}")
        except Exception as error:
            print(f"Error inesperado: {error}")

def imprimir_menu():
    print("\n===== MENÚ DEL CAJERO =====")
    print("1. Verificar saldo")
    print("2. Depositar dinero")
    print("3. Retirar dinero")
    print("4. Salir")
    print("Seleccione una opción: ", end="")


def leer_opcion_entera():
    try:
        return int(input().strip())
    except ValueError:
        raise ValueError("Error: Ingrese una opcion entera valida")


def procesar_opcion(opcion):
    if opcion == 1:
        consultar_saldo()
    elif opcion == 2:
        monto = leer_monto()
        depositar(monto)
    elif opcion == 3:
        monto = leer_monto()
        retirar(monto)
    elif opcion == 4:
        print("Gracias por usar el cajero automático.")
    else:
        raise ValueError(f"Opcion '{opcion}' no es valida. Por favor, elige entre 1 y 4.")


def consultar_saldo():
    print(f"Saldo actual: S/.{saldo:.2f}")


def depositar(monto):
    global saldo

    validar_monto_positivo(monto)

    saldo += monto
    print(f"Deposito exitoso, nuevo saldo: S/.{saldo:.2f}")


def retirar(monto):
    global saldo

    validar_monto_positivo(monto)
    validar_fondos_suficientes(monto)

    saldo -= monto
    print(f"Retiro exitoso")


def leer_monto():
    try:
        return float(input().strip())
    except ValueError:
        raise ValueError("El valor introducido no es un numero valido")


def validar_monto_positivo(monto):
    if monto <= 0:
        raise ValueError(f"El importe debe ser mayor que cero")


def validar_fondos_suficientes(monto):
    if monto > saldo:
        raise ValueError(
            f"Fondos insuficientes, el importe solicitado es mayor al saldo disponible"
        )

if __name__ == "__main__":
    principal()