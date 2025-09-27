package com.esporte.myapp.dto;

public record AvaliationResponseRequest(
        String fromUserId,
        Integer rating,
        String comment,
        String skillLevel
) {}