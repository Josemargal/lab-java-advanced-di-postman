package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.EarlyBirdDiscountService;
import com.example.demo.service.EarlyBirdDiscountService.DiscountResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DiscountController {

    private final EarlyBirdDiscountService discountService;

    // Inyección de dependencia basada en constructor
    @Autowired(required = false)
    public DiscountController(EarlyBirdDiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping("/discount")
    public ResponseEntity<?> calculateDiscount(
            @RequestParam String eventDate,
            @RequestParam String bookingDate) {

        // Verificar si el servicio está disponible
        if (discountService == null) {
            return ResponseEntity.ok(createResponse(
                    0.0,
                    "La característica de descuento por reserva anticipada está deshabilitada",
                    false
            ));
        }

        try {
            // Convertir strings de fecha a objetos LocalDate
            LocalDate eventLocalDate = LocalDate.parse(eventDate, DateTimeFormatter.ISO_DATE);
            LocalDate bookingLocalDate = LocalDate.parse(bookingDate, DateTimeFormatter.ISO_DATE);

            // Calcular el descuento
            DiscountResult result = discountService.calculateDiscount(eventLocalDate, bookingLocalDate);

            // Preparar la respuesta
            return ResponseEntity.ok(createResponse(
                    result.getDiscountPercentage(),
                    result.getMessage(),
                    true
            ));

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(createResponse(
                    0.0,
                    "Formato de fecha inválido. Use formato ISO (YYYY-MM-DD)",
                    false
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(createResponse(
                    0.0,
                    "Error al procesar la solicitud: " + e.getMessage(),
                    false
            ));
        }
    }

    private Map<String, Object> createResponse(double discountPercentage, String message, boolean featureEnabled) {
        Map<String, Object> response = new HashMap<>();
        response.put("discountPercentage", discountPercentage);
        response.put("message", message);
        response.put("featureEnabled", featureEnabled);
        return response;
    }
}
