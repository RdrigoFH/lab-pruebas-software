import re

def validar_contrasena(contrasena: str) -> dict:
    errores = []

    # Validar argumento vacío o None
    if contrasena is None or contrasena == "":
        return {
            "valida": False,
            "errores": [
                "Debe tener al menos 8 caracteres",
                "Debe contener al menos una letra mayúscula",
                "Debe contener al menos una letra minúscula",
                "Debe contener al menos un número",
                "Debe contener al menos un carácter especial"
            ]
        }

    if len(contrasena) < 8:
        errores.append("Debe tener al menos 8 caracteres")

    if not re.search(r"[A-Z]", contrasena):
        errores.append("Debe contener al menos una letra mayúscula")

    if not re.search(r"[a-z]", contrasena):
        errores.append("Debe contener al menos una letra minúscula")

    if not re.search(r"\d", contrasena):
        errores.append("Debe contener al menos un número")

    if not re.search(r"[!@#$%^&*]", contrasena):
        errores.append("Debe contener al menos un carácter especial")

    return {
        "valida": len(errores) == 0,
        "errores": errores
    }