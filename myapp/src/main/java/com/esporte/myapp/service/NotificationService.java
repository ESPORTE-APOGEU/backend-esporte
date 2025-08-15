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

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repo;
    private final UserRepository userRepo;

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

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
        // se não vier do front, garante timestamp agora
        if (n.getTimestamp() == null) n.setTimestamp(LocalDateTime.now());

        n = repo.save(n);
        return toResponse(n);
    }

    public List<NotificationResponse> listByUser(Long userId) {
        return repo.findByUser_IdOrderByTimestampDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
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
                n.getTimestamp() != null ? n.getTimestamp().format(ISO) : null,
                n.getTagText(),
                n.getTagIcon(),
                n.getRelatedEventId()
        );
    }
}
