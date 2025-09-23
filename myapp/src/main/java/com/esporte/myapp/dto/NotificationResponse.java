// src/main/java/com/esporte/myapp/dto/NotificationResponse.java
package com.esporte.myapp.dto;

import com.esporte.myapp.entity.Notification;
import com.esporte.myapp.entity.User;
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
        Long entryId,
        String actorId,
        String actorName,
        String actorPhoto
) {
    public static NotificationResponse from(Notification n) {
        User actor = (n.getRelatedEntry() != null) ? n.getRelatedEntry().getUser() : null;

        // Fallbacks vindos do snapshot gravado na entry
        String snapshotName  = (n.getRelatedEntry() != null) ? n.getRelatedEntry().getRequesterName()  : null;
        String snapshotPhoto = (n.getRelatedEntry() != null) ? n.getRelatedEntry().getRequesterPhoto() : null;

        String finalName  = (actor != null && actor.getName()  != null && !actor.getName().isBlank())
                ? actor.getName()
                : snapshotName;
        String finalPhoto = (actor != null && actor.getPhoto() != null && !actor.getPhoto().isBlank())
                ? actor.getPhoto()
                : snapshotPhoto;

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
                n.getRelatedEntry() != null ? n.getRelatedEntry().getId() : null,
                actor != null ? actor.getId() : null,
                finalName,
                finalPhoto
        );
    }
}
