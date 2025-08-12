package com.esporte.myapp.service;

import com.esporte.myapp.dto.NotificationResponse;
import com.esporte.myapp.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repo;

    public List<NotificationResponse> listByUser(Long userId) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return repo.findByUserIdOrderByTimestampDesc(userId).stream()
            .map(n -> new NotificationResponse(
                n.getId(),
                n.getType(),
                n.getIconName(),
                n.getTitle(),
                n.getDescription(),
                n.getTimestamp().format(fmt),
                n.getTagText() != null ? new NotificationResponse.Tag(n.getTagText(), n.getTagIcon()) : null,
                null, // userImage pode ser preenchido se necessário
                null  // userName idem
            ))
            .collect(Collectors.toList());
    }
}