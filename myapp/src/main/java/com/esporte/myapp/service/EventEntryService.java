package com.esporte.myapp.service;

import com.esporte.myapp.dto.EventEntryRequest;
import com.esporte.myapp.dto.EventEntryResponse;
import com.esporte.myapp.dto.EventRequest;
import com.esporte.myapp.dto.EventResponse;
import com.esporte.myapp.dto.UserResponse;
import com.esporte.myapp.dto.RemainingSlotsResponse;
import com.esporte.myapp.entity.EventEntry;
import com.esporte.myapp.entity.Event;
import com.esporte.myapp.entity.Notification;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.EventEntryRepository;
import com.esporte.myapp.repository.EventRepository;
import com.esporte.myapp.repository.NotificationRepository;
import com.esporte.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventEntryService {
    private final EventEntryRepository repo;
    private final EventRepository repository;
    private final UserRepository userRepository; 
    private final NotificationRepository notificationRepository; // injetado

    public EventEntryResponse requestEntry(EventEntryRequest req) {
        EventEntry entry = new EventEntry();
        entry.setEventId(req.getEventId());
        userRepository.findById(req.getUserId())
            .ifPresentOrElse(entry::setUser, () -> { throw new RuntimeException("Usuário não encontrado"); });
        entry.setRequestedAt(LocalDateTime.now());
        entry.setStatus("PENDING");
        entry = repo.save(entry);

        Event event = repository.findById(req.getEventId())
            .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        User organizer = userRepository.findById(event.getOrganizerId())
            .orElseThrow(() -> new RuntimeException("Organizador não encontrado"));

        // Criação da notificação para o organizador
        Notification notification = new Notification();
        notification.setUser(organizer);
        notification.setType("entry_request");
        notification.setIconName("info");
        notification.setTitle("Pedido de entrada");
        notification.setDescription("Um usuário solicitou entrada no evento: " + event.getName());
        notification.setTimestamp(LocalDateTime.now());
        notification.setRelatedEventId(event.getId());
        // Armazene também o id da entrada pendente (necessário para aceitar/recusar)
        notification.setEntryId(entry.getId()); // ← nova propriedade

        notificationRepository.save(notification);

        EventEntryResponse res = new EventEntryResponse();
        res.setMessage("Entrada solicitada com sucesso! Aguarde a resposta do organizador.");
        res.setEntryId(entry.getId());
        return res;
    }

    // Novo método para o organizador aceitar o pedido
    @Transactional
    public void acceptEntry(Long entryId) {
        EventEntry entry = repo.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Pedido de entrada não encontrado"));
        entry.setStatus("ACCEPTED");
        repo.save(entry);

        User participant = entry.getUser();
        Event event = repository.findById(entry.getEventId())
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        // remove a notificação de pedido do organizador
        notificationRepository.deleteByEntryIdAndTypeAndUser_Id(entryId, "entry_request", event.getOrganizerId());

        Notification notification = new Notification();
        notification.setUser(participant);
        notification.setType("entry_accepted");
        notification.setIconName("whatsapp");
        notification.setTitle("Você foi aceito no evento!");
        notification.setDescription("Você foi aceito no evento " + event.getName() + "!");
        notification.setTimestamp(LocalDateTime.now());
        notification.setRelatedEventId(event.getId());
        notification.setEntryId(entry.getId());
        notificationRepository.save(notification);
    }

    // Pode-se adicionar também um método para recusar a entrada (declineEntry)

    public EventResponse create(EventRequest req) {
        if (req == null) throw new IllegalArgumentException("Corpo da requisição é obrigatório");
        if (req.name() == null || req.name().isBlank()) throw new IllegalArgumentException("name é obrigatório");
        if (req.location() == null || req.location().isBlank()) throw new IllegalArgumentException("location é obrigatório");
        // date e times são tipos java.time, então valide apenas null
        if (req.date() == null) throw new IllegalArgumentException("date é obrigatório");
        if (req.startTime() == null) throw new IllegalArgumentException("startTime é obrigatório");

        Event e = new Event();
        e.setName(req.name());
        e.setLocation(req.location());
        e.setSport(req.sport());
        e.setLevel(req.level());
        e.setGender(req.gender());
        e.setDescription(req.description());
        e.setPrice(req.price());
        e.setOrganizerId(req.organizerId());
        e.setOrganizerPhoto(req.organizerPhoto());

        // Campos já vêm como LocalDate/LocalTime
        e.setDate(req.date());
        if (req.startTime() != null) e.setStartTime(req.startTime());
        if (req.endTime() != null) e.setEndTime(req.endTime());

        // capacidade definida na criação (se enviada)
        if (req.capacity() != null) {
            e.setCapacity(req.capacity());
        }

        // capacity pode ser definido depois via PATCH /events/{id}/capacity
        e = repository.save(e);
        return EventResponse.from(e);
    }

    public EventResponse get(Long id) {
        Event event = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        return EventResponse.from(event);
    }

    public List<EventResponse> listAll() {
        return repository.findAll()
                         .stream()
                         .map(EventResponse::from)
                         .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getParticipants(Long eventId) {
        // Busca com fetch join para inicializar User dentro da transação
        List<EventEntry> entries = repo.findByEventIdAndStatusFetchUser(eventId, "ACCEPTED");
        return entries.stream()
            .map(e -> {
                var u = e.getUser();
                return new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getCreatedAt(), u.getPhoto());
            })
            .collect(Collectors.toList());
    }

    public record EventEntrySummary(Long id, Long userId, String status) {}
    public List<EventEntrySummary> entriesByEvent(Long eventId) {
        return repo.findByEventId(eventId).stream()
            .map(e -> new EventEntrySummary(e.getId(), e.getUser() != null ? e.getUser().getId() : null, e.getStatus()))
            .collect(Collectors.toList());
    }

    public void declineEntry(Long entryId) {
        EventEntry entry = repo.findById(entryId)
            .orElseThrow(() -> new RuntimeException("Pedido de entrada não encontrado"));
        entry.setStatus("DECLINED");
        repo.save(entry);

        User participant = entry.getUser();
        Event event = repository.findById(entry.getEventId())
            .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        // remove a notificação de pedido do organizador
        notificationRepository.deleteByEntryIdAndTypeAndUser_Id(entryId, "entry_request", event.getOrganizerId());

        Notification notif = new Notification();
        notif.setUser(participant);
        notif.setType("entry_declined");
        notif.setIconName("info");
        notif.setTitle("Pedido recusado");
        notif.setDescription("Seu pedido de entrada no evento \"" + event.getName() + "\" foi recusado.");
        notif.setTimestamp(LocalDateTime.now());
        notif.setRelatedEventId(event.getId());
        notif.setEntryId(entry.getId());
        notificationRepository.save(notif);
    }

    public com.esporte.myapp.dto.RemainingSlotsResponse getRemainingSlots(Long eventId) {
        Event event = repository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        int capacity = event.getCapacity() != null ? event.getCapacity() : 10;
        int accepted = repo.findByEventIdAndStatus(eventId, "ACCEPTED").size();
        int remaining = Math.max(capacity - accepted, 0);
        return new RemainingSlotsResponse(capacity, remaining);
    }

    public com.esporte.myapp.dto.RemainingSlotsResponse updateCapacity(Long eventId, Integer capacity) {
        Event event = repository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        event.setCapacity(capacity);
        repository.save(event);
        return getRemainingSlots(eventId);
    }
}