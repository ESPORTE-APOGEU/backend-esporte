package com.esporte.myapp.controller;

import com.esporte.myapp.dto.NotificationResponse;
import com.esporte.myapp.entity.Notification;
import com.esporte.myapp.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationRepository notificationRepository;

    @GetMapping("/{userId}/notifications")
    public List<NotificationResponse> getUserNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationRepository.findByUser_IdOrderByTimestampDesc(userId);
        return notifications.stream()
            .map(n -> new NotificationResponse(
                n.getId(),
                n.getType(),
                n.getIconName(),
                n.getTitle(),
                n.getDescription(),
                n.getTimestamp().toString(),
                null,
                null,
                (n.getUser() != null ? n.getUser().getName() : null)
            ))
            .collect(Collectors.toList());
    }
}