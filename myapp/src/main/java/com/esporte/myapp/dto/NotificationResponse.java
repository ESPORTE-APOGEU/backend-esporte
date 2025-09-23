// src/main/java/com/esporte/myapp/dto/NotificationResponse.java
package com.esporte.myapp.dto;

import java.time.format.DateTimeFormatter;
import com.esporte.myapp.entity.Notification;

public class NotificationResponse {
    private Long id;
    private String type;
    private String iconName;
    private String title;
    private String description;
    private String timestamp;
    private String tagText;
    private String tagIcon;
    private Long relatedEventId;
    private Long entryId;
    
    public NotificationResponse(
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
        this.id = id;
        this.type = type;
        this.iconName = iconName;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.tagText = tagText;
        this.tagIcon = tagIcon;
        this.relatedEventId = relatedEventId;
        this.entryId = entryId;
    }
    
    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getIconName() {
        return iconName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTagText() {
        return tagText;
    }

    public String getTagIcon() {
        return tagIcon;
    }

    public Long getRelatedEventId() {
        return relatedEventId;
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    @SuppressWarnings("unused")
    private NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(
            n.getId(),
            n.getType(),
            n.getIconName(),
            n.getTitle(),
            n.getDescription(),
            n.getTimestamp() != null ? n.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null,
            n.getTagText(),
            n.getTagIcon(),
            n.getRelatedEventId(),
            n.getEntryId()
        );
    }
}
