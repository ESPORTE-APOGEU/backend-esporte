package com.esporte.myapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank String id, // ID do Clerk
        @NotBlank String name,
        @Email @NotBlank String email,
        LocalDate birthday,
        Gender gender,
        String city,
        List<String> sports
) {}

