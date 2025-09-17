package com.esporte.myapp.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record NotificationRequest(
        @NotBlank String type,
        String iconName,
        @NotBlank String title,
        @NotBlank String description,
        String tagText,
        String tagIcon,
        Long relatedEventId,
        LocalDateTime timestamp,
        Long entryId
        // Se quiser mandar timestamp do front: LocalDateTime timestamp
) {}
