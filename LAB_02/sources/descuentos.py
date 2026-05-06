def calcular_descuento_extras(num_extras):
    """
    Calcula el porcentaje de descuento basado en la cantidad de extras.  
    Reglas:
    - 5 o más extras: 15%
    - 3 o más extras: 10%
    - Menos de 3: 0%
    """
    # Paso 3: REFACTOR - Validación de robustez
    if not isinstance(num_extras, int) or num_extras < 0:
        raise ValueError("El número de extras debe ser un entero positivo")

    # Paso 3: REFACTOR - Corrección de la lógica
    # Las condiciones más restrictivas (>= 5) deben evaluarse primero
    if num_extras >= 5:
        return 15
    elif num_extras >= 3:
        return 10
    
    return 0