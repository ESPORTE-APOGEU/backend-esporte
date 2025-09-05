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
) {

    public double longitude() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'longitude'");
    }

    public double latitude() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'latitude'");
    }}