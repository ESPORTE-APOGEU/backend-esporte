package com.esporte.myapp.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record EventFilterRequest(
     String name,
     List<String> sports,
     List<String> levels,
     String gender,            // <<< NOVO
     LocalDate date,
     LocalTime startTime,
     LocalTime endTime,
     Double latitude,
     Double longitude,
     Double maxDistanceKm
 ) {}