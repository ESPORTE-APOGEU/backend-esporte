package com.esporte.myapp.controller;

import com.esporte.myapp.dto.NotificationResponse;
import com.esporte.myapp.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService service;

    @GetMapping
    public List<NotificationResponse> list(@PathVariable Long userId) {
        return service.listByUser(userId);
    }
}