package com.esporte.myapp.controller;

import com.esporte.myapp.dto.NotificationRequest;
import com.esporte.myapp.dto.NotificationResponse;
import com.esporte.myapp.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    // Lista minhas notificações
    @GetMapping
    public List<NotificationResponse> listMine(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt != null ? jwt.getSubject() : null;
        if (userId == null) throw new RuntimeException("Unauthorized");
        return service.listMine(userId);
    }

    // Detalhe de uma notificação minha
    @GetMapping("/{id}")
    public NotificationResponse getOne(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id
    ) {
        String userId = jwt != null ? jwt.getSubject() : null;
        if (userId == null) throw new RuntimeException("Unauthorized");
        return service.getOneMine(userId, id);
    }

    // Opcional: permitir que o próprio usuário crie notificações (se fizer sentido)
    @PostMapping
    public ResponseEntity<NotificationResponse> create(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody NotificationRequest req
    ) {
        String userId = jwt != null ? jwt.getSubject() : null;
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(userId, req));
    }
}
