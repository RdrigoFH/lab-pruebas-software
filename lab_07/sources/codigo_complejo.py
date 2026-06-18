def validar_sistema_seguridad(password, usuario, intentos, ip_bloqueada, admin):
    """
    Función con Complejidad Ciclomática (CC) = 12
    Cálculo: 1 (base) + 11 estructuras de control = 12
    """
    score = 0

    # 1. Primer punto de decisión (if)
    if ip_bloqueada:
        return "Acceso denegado: IP Bloqueada"

    # 2. Segundo punto de decisión (if)
    if intentos > 3:
        return "Cuenta temporalmente suspendida"

    # 3. Tercer punto de decisión (if)
    if len(password) < 8:
        return "Contraseña demasiado corta"

    # 4-7. Cuatro bucles para analizar caracteres (+4 CC)
    for car in password:       # Corregido: 'in' en lugar de 'en'
        if car.isupper():      # 5. Punto de decisión (+1 CC)
            score += 2
        if car.isdigit():      # 6. Punto de decisión (+1 CC)
            score += 2
        if car in "@#$%&*":    # 7. Punto de decisión (+1 CC)
            score += 3

    # 8. Octavo punto de decisión (if)
    if usuario == "admin":
        # 9. Noveno punto de decisión (if anidado, +1 CC)
        if not admin:
            return "Fraude: Nombre admin sin privilegios"
        score += 5

    # 10. Décimo punto de decisión (if)
    if score < 10:
        return "Contraseña insecure"
    # 11. Undécimo punto de decisión (elif)
    elif score < 15:
        return "Seguridad Media"
    else:
        return "Seguridad Alta"


# --- BLOQUE MAIN PARA PRUEBAS ---
if __name__ == "__main__":
    # Caso de prueba que ejecuta el flujo completo
    resultado = validar_sistema_seguridad("P@ss123", "admin", 0, True, True)
    print(f"Resultado de la evaluación: {resultado}")
