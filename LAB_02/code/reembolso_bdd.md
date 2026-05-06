Feature: Reembolso de reserva de hotel

  Scenario: Cliente VIP cancela con poca antelación
    Given un cliente VIP
    And una reserva con un monto de 1000
    When el cliente cancela la reserva con 2 horas de antelación
    Then el reembolso debe ser del 50%, es decir 500
