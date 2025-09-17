// src/main/java/com/esporte/myapp/dto/UserRequest.java
package com.esporte.myapp.dto;

import com.esporte.myapp.enums.Gender;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;


public record UserRequest(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String id, // ID do Clerk ou seu próprio ID externo (opcional)
        @NotBlank String name,
        @Email @NotBlank String email,
        LocalDate birthday,
        Gender gender,
        String city,
        List<String> sports
) {}
