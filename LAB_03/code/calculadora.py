from functools import wraps

def _verificar_argumentos(func):
    @wraps(func)
    def wrapper(*args):
        if len(args) < 2:
            raise ValueError("Se requieren al menos dos argumentos")
        for arg in args:
            if isinstance(arg, bool) or not isinstance(arg, (int, float)):
                raise TypeError(f"El argumento '{arg}' no es un numero")
        return func(*args)
    return wrapper

@_verificar_argumentos
def sumar(*args):
    return sum(args)

@_verificar_argumentos 
def restar(*args):
    resultado = args[0]
    for arg in args[1:]:
        resultado -= arg
    return resultado

@_verificar_argumentos
def multiplicar(*args):
    resultado = 1
    for arg in args:
        resultado *= arg
    return resultado

@_verificar_argumentos
def dividir(*args):
    resultado = args[0]   
    for arg in args[1:]:
        if arg == 0:
            raise ValueError("No se puede dividir por cero")
        resultado /= arg
    return resultado

