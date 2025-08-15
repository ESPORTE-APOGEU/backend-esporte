package com.esporte.myapp.dto;

public record NotificationResponse(
    Long id,
    String type,
    String iconName,
    String title,
    String description,
    String timestamp,
    Tag tag,
    String userImage,
    String userName
) {
    public NotificationResponse(Long id2, String type2, String iconName2, String title2, String description2,
            String string, String tagText, String tagIcon, Long relatedEventId) {
        this(id2, type2, iconName2, title2, description2, string, new Tag(tagText, tagIcon), null, null);
    }

    public record Tag(String text, String icon) {}
}