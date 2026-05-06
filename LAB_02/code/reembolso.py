def calcular_reembolso(monto, horas, es_vip):
    try:
        monto_float = float(monto)
        horas_float = float(horas)
    except ValueError:
        raise ValueError("El monto y las horas deben ser numeros")
        
    if monto_float < 0:
        raise ValueError("El monto de la reserva no puede ser negativo")
    if horas_float < 0:
        raise ValueError("Las horas de antelacion no pueden ser negativas")

    porcentaje_reembolso = 0.0
    
    if horas_float > 72:
        porcentaje_reembolso = 1.0
    elif 24 <= horas_float <= 72:
        porcentaje_reembolso = 0.5
    else:
        porcentaje_reembolso = 0.0
        
    if es_vip and porcentaje_reembolso < 0.5:
        porcentaje_reembolso = 0.5
        
    return monto_float * porcentaje_reembolso

