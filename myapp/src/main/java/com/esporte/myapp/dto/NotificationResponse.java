// src/main/java/com/esporte/myapp/dto/NotificationResponse.java
package com.esporte.myapp.dto;

public record NotificationResponse(
        Long id,
        String type,
        String iconName,
        String title,
        String description,
        String timestamp,   // ISO string para o front
        String tagText,
        String tagIcon,
        Long relatedEventId
) {}
