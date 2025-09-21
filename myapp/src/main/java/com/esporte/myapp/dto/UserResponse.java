package com.esporte.myapp.dto;

import com.esporte.myapp.enums.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record UserResponse(
        String id,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDate birthday,
        Gender gender,
        String city,
        List<SportDTO> sports,
        String photo
) {}
