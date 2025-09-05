// src/main/java/com/esporte/myapp/dto/UserRequest.java
package com.esporte.myapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

import com.esporte.myapp.enums.Gender;

import java.time.LocalDate;
import java.util.List;

public record UserRequest(
        @NotBlank String id, // ID do Clerk ou seu próprio ID externo (opcional)
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank String password,     // <-- ADICIONADO
        LocalDate birthday,
        Gender gender,
        String city,
        List<String> sports
) {}
