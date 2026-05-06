def validar_password(password):
    # Verificación de longitud mínima (Requisito 1)
    tiene_longitud = len(password) >= 8
    
    # Verificación de presencia de número (Requisito 2)
    tiene_numero = any(char.isdigit() for char in password)
    
    return tiene_longitud and tiene_numero