package com.esporte.myapp.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventRequest(
    String name,
    String location,
    String sport,
    String level,
    String gender,
    LocalDate date,
    LocalTime startTime,
    LocalTime endTime,
    java.math.BigDecimal price,
    String description,
    Long organizerId,
    String organizerPhoto
) {}