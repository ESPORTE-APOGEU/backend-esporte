package com.esporte.myapp.dto;

import com.esporte.myapp.entity.Notification;
import java.time.format.DateTimeFormatter;

public record NotificationResponse(
        Long id,
        String type,
        String iconName,
        String title,
        String description,
        String timestamp,
        String tagText,
        String tagIcon,
        Long relatedEventId,
        Long entryId
) {
    public static NotificationResponse from(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getType(),
                n.getIconName(),
                n.getTitle(),
                n.getDescription(),
                n.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                n.getTagText(),
                n.getTagIcon(),
                n.getRelatedEvent() != null ? n.getRelatedEvent().getId() : null,
                n.getRelatedEntry() != null ? n.getRelatedEntry().getId() : null
        );
    }
}
