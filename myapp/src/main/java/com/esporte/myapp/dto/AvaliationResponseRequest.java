package com.esporte.myapp.dto;

public record AvaliationResponseRequest(
        Long fromUserId,
        Integer rating,
        String comment,
        String skillLevel
) {}
