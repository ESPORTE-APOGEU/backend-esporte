// src/main/java/com/esporte/myapp/dto/UserEventItemResponse.java
package com.esporte.myapp.dto;

import com.esporte.myapp.entity.Event;
import java.math.BigDecimal;

public record UserEventItemResponse(
        Long id,
        String name,
        String location,
        String sport,
        String level,
        String gender,
        String date,       // ISO yyyy-MM-dd
        String startTime,  // HH:mm:ss
        String endTime,    // HH:mm:ss
        BigDecimal price,
        String description
) {
    public static UserEventItemResponse from(Event e) {
        return new UserEventItemResponse(
                e.getId(),
                e.getName(),
                e.getLocation(),
                e.getSport(),
                e.getLevel(),
                e.getGender(),
                e.getDate() != null ? e.getDate().toString() : null,
                e.getStartTime() != null ? e.getStartTime().toString() : null,
                e.getEndTime() != null ? e.getEndTime().toString() : null,
                e.getPrice(),
                e.getDescription()
        );
    }
}
