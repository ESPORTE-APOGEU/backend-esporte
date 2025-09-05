package com.esporte.myapp.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventRequest(
<<<<<<< HEAD
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
<<<<<<< HEAD
) {

    public double longitude() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'longitude'");
    }

    public double latitude() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'latitude'");
    }}
=======
        @NotBlank String name,
        @NotBlank String location,
        @NotBlank String sport,
        @NotBlank String level,
        @NotBlank String gender,
        @NotNull LocalDate date,
        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        String description
) {}
>>>>>>> parent of 54d1e99 (Merge branch 'dev' into origin/feat/back-amizade)
<<<<<<< HEAD
=======
=======
        String description,
        @NotNull Double latitude,
        @NotNull Double longitude
) {}
>>>>>>> parent of 22174b3 (.)
=======
) {}
>>>>>>> parent of 3e4dcc1 (.)
>>>>>>> parent of 8c9b8c2 (Revert "revertendo")
