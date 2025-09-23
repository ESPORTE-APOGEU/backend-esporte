// src/main/java/com/esporte/myapp/service/NotificationService.java
package com.esporte.myapp.service;

import com.esporte.myapp.dto.NotificationRequest;
import com.esporte.myapp.dto.NotificationResponse;
import com.esporte.myapp.entity.Notification;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.NotificationRepository;
import com.esporte.myapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repo;
    private final UserRepository userRepo;

    public NotificationResponse create(Long userId, NotificationRequest req) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Notification n = new Notification();
        n.setUser(user);
        n.setType(req.type());
        n.setIconName(req.iconName());
        n.setTitle(req.title());
        n.setDescription(req.description());
        n.setTagText(req.tagText());
        n.setTagIcon(req.tagIcon());
        n.setRelatedEventId(req.relatedEventId());
        n.setEntryId(req.entryId()); // ← garantir persistência do entryId se vier
        if (n.getTimestamp() == null) n.setTimestamp(LocalDateTime.now());

        n = repo.save(n);
        return toResponse(n);
    }

    public List<NotificationResponse> listByUser(Long userId) {
        List<Notification> notifications = repo.findByUserId(userId);
        return notifications.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public NotificationResponse getOne(Long userId, Long id) {
        Notification n = repo.findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        return toResponse(n);
    }

    private NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getType(),
                n.getIconName(),
                n.getTitle(),
                n.getDescription(),
                n.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                n.getTagText(),
                n.getTagIcon(),
                n.getRelatedEventId(),
                n.getEntryId()       // ← use o campo real
        );
    }
}
