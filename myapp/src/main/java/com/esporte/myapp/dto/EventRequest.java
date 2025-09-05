package com.esporte.myapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record EventRequest(
    @NotBlank String name,
    @NotBlank String location,
    @NotBlank String sport,
    @NotBlank String level,
    @NotBlank String gender,
    @NotNull LocalDate date,
    @NotNull LocalTime startTime,
    @NotNull LocalTime endTime,
    @NotNull @DecimalMin("0.0") BigDecimal price,
    String description,
    @NotNull Long organizerId,
    String organizerPhoto
) {}
