package com.esporte.myapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record EventResponse(
        Long id,
        String name,
        String location,
        String sport,
        String level,
        String gender,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        BigDecimal price,
        String description,
        String organizerId,
        String organizerName,
        String organizerPhoto,
        String coverImageUrl
) {}
