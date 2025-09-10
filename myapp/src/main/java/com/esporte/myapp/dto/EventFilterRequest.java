package com.esporte.myapp.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record EventFilterRequest(
    Double latitude,
    Double longitude,
    Double maxDistanceKm,
    List<String> sports,
    List<String> levels,
    LocalDate date,
    LocalTime startTime,
    LocalTime endTime,
    String name
) {}
