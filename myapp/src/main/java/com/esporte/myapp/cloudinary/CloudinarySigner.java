package com.esporte.myapp.cloudinary;

import com.esporte.myapp.dto.CloudinarySignResponse;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

@Component
public class CloudinarySigner {
    private final String cloudName;
    private final String apiKey;
    private final String apiSecret;

    public CloudinarySigner(
            @org.springframework.beans.factory.annotation.Value("${cloudinary.cloudName}") String cloudName,
            @org.springframework.beans.factory.annotation.Value("${cloudinary.apiKey}") String apiKey,
            @org.springframework.beans.factory.annotation.Value("${cloudinary.apiSecret}") String apiSecret
    ) {
        this.cloudName = cloudName;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public CloudinarySignResponse sign(String folder, String publicId) {
        long timestamp = System.currentTimeMillis() / 1000L;

        Map<String, String> params = new TreeMap<>();
        params.put("timestamp", String.valueOf(timestamp));
        if (folder != null && !folder.isBlank()) params.put("folder", folder);
        if (publicId != null && !publicId.isBlank()) params.put("public_id", publicId);

        // stringToSign: "folder=events&public_id=abc&timestamp=1234567890"
        StringBuilder sb = new StringBuilder();
        for (var e : params.entrySet()) {
            if (sb.length() > 0) sb.append("&");
            sb.append(e.getKey()).append("=").append(e.getValue());
        }
        sb.append(apiSecret);

        String signature = sha1Hex(sb.toString());
        return new CloudinarySignResponse(cloudName, apiKey, timestamp, signature, folder, publicId);
    }

    private static String sha1Hex(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : bytes) hex.append(String.format("%02x", b));
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
