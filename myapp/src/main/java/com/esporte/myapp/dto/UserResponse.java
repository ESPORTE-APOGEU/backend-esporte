package com.esporte.myapp.dto;

import java.time.LocalDateTime;

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
