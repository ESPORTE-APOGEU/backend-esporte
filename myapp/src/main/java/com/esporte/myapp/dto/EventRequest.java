package com.esporte.myapp.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

// Este DTO agora reflete todos os campos do seu formulário
public record EventRequest(
        @NotBlank String name,
        @NotBlank String location,
        Double latitude,   // Opcional por enquanto
        Double longitude,  // Opcional por enquanto
        @NotBlank String sport, // Apenas um esporte
        @NotBlank String level,
        @NotBlank String gender,
        @NotNull @FutureOrPresent LocalDate date,
        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime,
        @NotNull @PositiveOrZero BigDecimal price,
        String description,
        String whatsappLink,
        boolean isPrivate,
        @NotNull @Min(1) Integer minParticipants,
        @NotNull @Min(1) Integer maxParticipants,
        String coverImageUrl
) {}