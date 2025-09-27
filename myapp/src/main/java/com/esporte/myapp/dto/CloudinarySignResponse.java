package com.esporte.myapp.dto;

public record CloudinarySignResponse(
        String cloudName,
        String apiKey,
        Long timestamp,
        String signature,
        String folder,
        String publicId
) {}