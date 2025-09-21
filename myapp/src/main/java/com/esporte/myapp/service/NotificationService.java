package com.esporte.myapp.service;

import com.esporte.myapp.dto.NotificationRequest;
import com.esporte.myapp.dto.NotificationResponse;
import com.esporte.myapp.entity.Event;
import com.esporte.myapp.entity.EventEntry;
import com.esporte.myapp.entity.Notification;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.EventEntryRepository;
import com.esporte.myapp.repository.EventRepository;
import com.esporte.myapp.repository.NotificationRepository;
import com.esporte.myapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repo;
    private final UserRepository userRepo;
    private final EventRepository eventRepo;
    private final EventEntryRepository entryRepo;

    /**
     * Cria notificação para o usuário autenticado (passado por parâmetro).
     * Útil para cenários em que o front cria notificações próprias.
     */
    @Transactional
    public NotificationResponse create(String userId, NotificationRequest req) {
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

        if (req.relatedEventId() != null) {
            Event ev = eventRepo.findById(req.relatedEventId())
                    .orElseThrow(() -> new EntityNotFoundException("Event not found"));
            n.setRelatedEvent(ev);
        }
        if (req.entryId() != null) {
            EventEntry entry = entryRepo.findById(req.entryId())
                    .orElseThrow(() -> new EntityNotFoundException("Entry not found"));
            n.setRelatedEntry(entry);
        }

        n.setTimestamp(req.timestamp() != null ? req.timestamp() : LocalDateTime.now());
        n = repo.save(n);
        return NotificationResponse.from(n);
    }

    /**
     * Método utilitário para criar notificações do sistema (usado por outros services).
     */
    @Transactional
    public void notifyUser(
            String targetUserId,
            String type,
            String title,
            String description,
            String iconName,
            String tagText,
            String tagIcon,
            Long relatedEventId,
            Long entryId
    ) {
        NotificationRequest req = new NotificationRequest(
                type, iconName, title, description, tagText, tagIcon, relatedEventId, entryId, null
        );
        create(targetUserId, req);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> listMine(String userId) {
        return repo.findByUser_IdOrderByTimestampDesc(userId)
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public NotificationResponse getOneMine(String userId, Long id) {
        Notification n = repo.findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        return NotificationResponse.from(n);
    }
}
