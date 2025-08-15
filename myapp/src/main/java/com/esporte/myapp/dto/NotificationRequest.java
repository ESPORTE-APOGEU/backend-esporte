package com.esporte.myapp.dto;

import jakarta.validation.constraints.NotBlank;

public record NotificationRequest(
        @NotBlank String type,
        String iconName,
        @NotBlank String title,
        @NotBlank String description,
        String tagText,
        String tagIcon,
        Long relatedEventId
        // Se quiser mandar timestamp do front: LocalDateTime timestamp
) {}
