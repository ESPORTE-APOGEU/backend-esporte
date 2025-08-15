package com.esporte.myapp.service;

import com.esporte.myapp.dto.EventEntryRequest;
import com.esporte.myapp.dto.EventEntryResponse;
import com.esporte.myapp.dto.EventRequest;
import com.esporte.myapp.dto.EventResponse;
import com.esporte.myapp.dto.UserResponse;
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

import java.time.LocalDateTime;
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
        // Busca o usuário solicitante e usa o relacionamento herdado
        userRepository.findById(req.getUserId())
              .ifPresentOrElse(entry::setUser, () -> {
                  throw new RuntimeException("Usuário não encontrado");
              });
        entry.setRequestedAt(LocalDateTime.now());
        entry.setStatus("PENDING");
        repo.save(entry);

        // Buscar o evento para obter os dados do organizador
        Event event = repository.findById(req.getEventId())
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        // Busca o usuário organizador por meio do organizerId contido em Event
        User organizer = userRepository.findById(event.getOrganizerId())
                .orElseThrow(() -> new RuntimeException("Organizador não encontrado"));

        // Cria a notificação para o organizador informando o novo pedido
        Notification notification = new Notification();
        notification.setUser(organizer);
        notification.setType("entry_request");
        notification.setIconName("info"); // EXISTENTE no front
        notification.setTitle("Pedido de entrada");
        notification.setDescription("Um usuário solicitou entrada no evento: " + event.getName());
        notification.setTimestamp(LocalDateTime.now());
        notification.setRelatedEventId(event.getId());
        notificationRepository.save(notification);

        EventEntryResponse res = new EventEntryResponse();
        res.setMessage("Entrada solicitada com sucesso! Aguarde a resposta do organizador.");
        return res;
    }

    // Novo método para o organizador aceitar o pedido
    public void acceptEntry(Long entryId) {
        EventEntry entry = repo.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Pedido de entrada não encontrado"));
        entry.setStatus("ACCEPTED");
        repo.save(entry);
        // Enviar notificação para o usuário solicitante
        User participant = entry.getUser();
        Event event = repository.findById(entry.getEventId())
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        Notification notification = new Notification();
        notification.setUser(participant);
        notification.setType("entry_accepted");
        notification.setIconName("info"); // EXISTENTE no front
        notification.setTitle("Entrada aceita!");
        notification.setDescription("Você foi aceito no evento: " + event.getName());
        notification.setTimestamp(LocalDateTime.now());
        notification.setRelatedEventId(event.getId());
        notificationRepository.save(notification);
    }

    // Pode-se adicionar também um método para recusar a entrada (declineEntry)

    public EventResponse create(EventRequest req) {
        Event event = new Event(
            null,
            req.name(), req.location(), req.sport(), req.level(), req.gender(),
            req.date(), req.startTime(), req.endTime(),
            req.price(), req.description(),
            req.organizerId(), req.organizerPhoto()
        );
        event = repository.save(event);
        return EventResponse.from(event);
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

    public List<UserResponse> getParticipants(Long eventId) {
        List<EventEntry> entries = repo.findByEventId(eventId);
        
        return entries.stream()
                  .map(entry -> {
                      if (entry.getUser() != null) {
                          var user = entry.getUser();
                          return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
                      } else {
                          return null;
                      }
                  })
                  .filter(response -> response != null)
                  .collect(Collectors.toList());
    }
}