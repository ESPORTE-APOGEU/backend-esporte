package com.esporte.myapp.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventRequest(
    String name,
    String location,
    String sport,
    String level,
    String gender,
    java.time.LocalDate date,
    java.time.LocalTime startTime,
    java.time.LocalTime endTime,
    Double price,
    String description,
    Long organizerId,
    String organizerPhoto,
    Integer capacity // novo
) {}