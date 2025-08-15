package com.esporte.myapp.service;

import com.esporte.myapp.dto.NotificationResponse;
import com.esporte.myapp.entity.Notification;
import com.esporte.myapp.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public List<NotificationResponse> listByUser(Long userId) {
        return notificationRepository.findByUser_IdOrderByTimestampDesc(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private NotificationResponse toResponse(Notification n) {
        // Ajuste aqui caso seu NotificationResponse tenha outra assinatura
        return new NotificationResponse(
                n.getId(),
                n.getType(),
                n.getIconName(),
                n.getTitle(),
                n.getDescription(),
                n.getTimestamp() != null ? n.getTimestamp().format(FORMATTER) : null,
                n.getTagText(),
                n.getTagIcon(),
                n.getRelatedEventId()
        );
    }
}