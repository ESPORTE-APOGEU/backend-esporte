package com.esporte.myapp.dto;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

import com.esporte.myapp.enums.Gender;

public record UserResponse(
        String id,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDate birthday,
        Gender gender,
        String city,
        List<String> sports
) {}
