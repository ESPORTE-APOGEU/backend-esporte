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
    public record Tag(String text, String icon) {}
}