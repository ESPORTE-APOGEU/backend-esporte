package com.esporte.myapp.controller;

import com.esporte.myapp.cloudinary.CloudinarySigner;
import com.esporte.myapp.dto.CloudinarySignRequest;
import com.esporte.myapp.dto.CloudinarySignResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/api/v1/uploads/cloudinary")
@RequiredArgsConstructor
public class CloudinaryController {
    private final CloudinarySigner signer;

    @PostMapping("/sign")
    public CloudinarySignResponse sign(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody CloudinarySignRequest req
    ) {
        if (jwt == null) throw new RuntimeException("Unauthorized");
        // opcional: validar permissões
        return signer.sign(req.folder(), req.publicId());
    }
}