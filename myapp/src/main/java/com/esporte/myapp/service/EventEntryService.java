package com.esporte.myapp.service;

import com.esporte.myapp.dto.EventEntryResponse;
import com.esporte.myapp.entity.EventEntry;
import com.esporte.myapp.entity.Event;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.enums.RequestStatus;
import com.esporte.myapp.repository.EventEntryRepository;
import com.esporte.myapp.repository.EventRepository;
import com.esporte.myapp.repository.NotificationRepository;
import com.esporte.myapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventEntryService {
    private final EventEntryRepository entryRepo;
    private final EventRepository eventRepo;
    private final UserRepository userRepo;
    private final NotificationService notificationService;

    public EventEntryResponse requestEntry(String requesterId, Long eventId) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

        User requester = userRepo.findById(requesterId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        // Não deixar o organizador pedir entrada no próprio evento
        if (event.getCreator() != null && requesterId.equals(event.getCreator().getId())) {
            throw new IllegalStateException("Organizador não pode solicitar entrada no próprio evento");
        }

        // Bloqueia duplicado (PENDING/ACCEPTED)
        Optional<EventEntry> existing = entryRepo.findByEvent_IdAndUser_Id(eventId, requesterId);
        if (existing.isPresent()) {
            RequestStatus st = existing.get().getStatus();
            if (st == RequestStatus.PENDING || st == RequestStatus.ACCEPTED) {
                throw new IllegalStateException("Já existe uma solicitação em andamento ou aceita para este evento");
            }
        }

        // Capacidade ao solicitar? normalmente só valida ao ACEITAR,
        // mas se quiser bloquear cedo, descomente:
        // if (isEventFull(event)) throw new IllegalStateException("Evento atingiu a capacidade máxima");

        EventEntry entry = new EventEntry();
        entry.setEvent(event);
        entry.setUser(requester);
        entry.setRequestedAt(LocalDateTime.now());
        entry.setStatus(RequestStatus.PENDING);

        entry = entryRepo.save(entry);

        // Notifica o organizador
        if (event.getCreator() != null) {
            notificationService.notifyUser(
                    event.getCreator().getId(),
                    "entry_request",
                    "Pedido de entrada",
                    "Um usuário solicitou entrada no evento: " + event.getName(),
                    "info",
                    null,
                    null,
                    event.getId(),
                    entry.getId()
            );
        }

        return EventEntryResponse.from(entry);
    }

    public void acceptEntry(Long entryId, String organizerId) {
        EventEntry entry = entryRepo.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de entrada não encontrado"));

        Event event = entry.getEvent();
        if (event == null) throw new EntityNotFoundException("Evento não encontrado");

        // Permissão: apenas o organizador pode aceitar
        if (event.getCreator() == null || !organizerId.equals(event.getCreator().getId())) {
            throw new IllegalStateException("Apenas o organizador pode aceitar pedidos deste evento");
        }

        if (entry.getStatus() == RequestStatus.ACCEPTED) return; // idempotente
        if (entry.getStatus() == RequestStatus.DECLINED) {
            throw new IllegalStateException("Pedido já foi recusado");
        }

        // Capacidade
        if (isEventFull(event)) {
            throw new IllegalStateException("Evento atingiu a capacidade máxima");
        }

        entry.setStatus(RequestStatus.ACCEPTED);
        entryRepo.save(entry);

        // Notifica o participante
        User participant = entry.getUser();
        if (participant != null) {
            notificationService.notifyUser(
                    entry.getUser().getId(),
                    "entry_accepted",
                    "Entrada aceita!",
                    "Você foi aceito no evento: " + event.getName(),
                    "info",
                    null,
                    null,
                    event.getId(),
                    entry.getId()
            );
        }
    }

    // Pode-se adicionar também um método para recusar a entrada (declineEntry)


    public void declineEntry(Long entryId, String organizerId) {
        EventEntry entry = entryRepo.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de entrada não encontrado"));

        Event event = entry.getEvent();
        if (event == null) throw new EntityNotFoundException("Evento não encontrado");

        // Permissão: apenas o organizador pode recusar
        if (event.getCreator() == null || !organizerId.equals(event.getCreator().getId())) {
            throw new IllegalStateException("Apenas o organizador pode recusar pedidos deste evento");
        }

        if (entry.getStatus() == RequestStatus.DECLINED) return; // idempotente

        entry.setStatus(RequestStatus.DECLINED);
        entryRepo.save(entry);

        // Notifica o participante
        User participant = entry.getUser();
        if (participant != null) {
            notificationService.notifyUser(
                    entry.getUser().getId(),
                    "entry_declined",
                    "Pedido recusado",
                    "Seu pedido de entrada no evento \"" + event.getName() + "\" foi recusado.",
                    "info",
                    null,
                    null,
                    event.getId(),
                    entry.getId()
            );
        }
    }

    @Transactional(readOnly = true)
    public Optional<EventEntryResponse> getMyEntry(String userId, Long eventId) {
        return entryRepo.findFirstByUser_IdAndEvent_Id(userId, eventId).map(EventEntryResponse::from);
    }

    private boolean isEventFull(Event event) {
        Integer max = event.getMaxParticipants(); // ajuste se o tipo for diferente
        if (max == null) return false;
        long accepted = entryRepo.countByEvent_IdAndStatus(event.getId(), RequestStatus.ACCEPTED);
        return accepted >= max;
    }
}