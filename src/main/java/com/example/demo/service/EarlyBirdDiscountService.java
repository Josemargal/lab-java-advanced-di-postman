package com.example.demo.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

@Service
public class EarlyBirdDiscountService {

    private static final int DAYS_FOR_DISCOUNT = 30;
    private static final double DISCOUNT_PERCENTAGE = 15.0;

    /**
     * Calcula el descuento basado en la fecha de reserva en relación con la fecha del evento.
     *
     * @param eventDate Fecha del evento
     * @param bookingDate Fecha de la reserva
     * @return Objeto con información sobre el descuento aplicado
     */
    public DiscountResult calculateDiscount(LocalDate eventDate, LocalDate bookingDate) {
        // Validar que las fechas no sean nulas
        if (eventDate == null || bookingDate == null) {
            return new DiscountResult(0.0, "Las fechas proporcionadas no son válidas");
        }

        // Verificar que la fecha de reserva no sea posterior a la fecha del evento
        if (bookingDate.isAfter(eventDate)) {
            return new DiscountResult(0.0, "La fecha de reserva no puede ser posterior a la fecha del evento");
        }

        // Calcular días entre la reserva y el evento
        long daysBetween = ChronoUnit.DAYS.between(bookingDate, eventDate);

        // Aplicar descuento si la reserva se realiza con suficiente antelación
        if (daysBetween >= DAYS_FOR_DISCOUNT) {
            return new DiscountResult(
                    DISCOUNT_PERCENTAGE,
                    String.format("Descuento del %.1f%% aplicado por reserva anticipada (%d días antes)",
                            DISCOUNT_PERCENTAGE, daysBetween)
            );
        } else {
            return new DiscountResult(
                    0.0,
                    String.format("No hay descuento disponible. Se requieren al menos %d días de antelación (actualmente: %d días)",
                            DAYS_FOR_DISCOUNT, daysBetween)
            );
        }
    }

    /**
     * Clase interna para representar el resultado del cálculo de descuento
     */
    public static class DiscountResult {
        private final double discountPercentage;
        private final String message;

        public DiscountResult(double discountPercentage, String message) {
            this.discountPercentage = discountPercentage;
            this.message = message;
        }

        public double getDiscountPercentage() {
            return discountPercentage;
        }

        public String getMessage() {
            return message;
        }
    }
}
