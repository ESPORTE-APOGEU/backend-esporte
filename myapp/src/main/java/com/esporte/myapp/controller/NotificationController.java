// src/main/java/com/esporte/myapp/controller/NotificationController.java
package com.esporte.myapp.controller;

import com.esporte.myapp.dto.NotificationRequest;
import com.esporte.myapp.dto.NotificationResponse;
import com.esporte.myapp.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @PostMapping("/{userId}/notifications")
    public ResponseEntity<NotificationResponse> create(
            @PathVariable Long userId,
            @Valid @RequestBody NotificationRequest req
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(userId, req));
    }

    @GetMapping("/{userId}/notifications")
    public List<NotificationResponse> list(@PathVariable Long userId) {
        return service.listByUser(userId);
    }

    @GetMapping("/{userId}/notifications/{id}")
    public NotificationResponse getOne(@PathVariable Long userId, @PathVariable Long id) {
        return service.getOne(userId, id);
    }
}
