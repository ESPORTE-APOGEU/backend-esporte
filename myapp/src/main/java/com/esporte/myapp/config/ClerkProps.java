package com.esporte.myapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "clerk")
public record ClerkProps(
        String issuer,
        String jwksUri,
        String audience
) {}
