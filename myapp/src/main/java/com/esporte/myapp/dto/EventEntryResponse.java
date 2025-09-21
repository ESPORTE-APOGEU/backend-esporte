package com.esporte.myapp.dto;

import com.esporte.myapp.entity.EventEntry;
import com.esporte.myapp.enums.RequestStatus;

import java.time.LocalDateTime;

public record EventEntryResponse(
        Long id,
        String userId,
        Long eventId,
        RequestStatus status,
        LocalDateTime requestedAt
) {
    public static EventEntryResponse from(EventEntry e) {
        return new EventEntryResponse(
                e.getId(),
                e.getUser() != null ? e.getUser().getId() : null,
                e.getEvent() != null ? e.getEvent().getId() : null,
                e.getStatus(),
                e.getRequestedAt()
        );
    }
}
