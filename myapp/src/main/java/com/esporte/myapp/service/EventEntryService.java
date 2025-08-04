package com.esporte.myapp.service;

import com.esporte.myapp.dto.EventEntryRequest;
import com.esporte.myapp.dto.EventEntryResponse;
import com.esporte.myapp.dto.EventRequest;
import com.esporte.myapp.dto.EventResponse;
import com.esporte.myapp.dto.UserResponse;
import com.esporte.myapp.entity.EventEntry;
import com.esporte.myapp.entity.Event;
import com.esporte.myapp.repository.EventEntryRepository;
import com.esporte.myapp.repository.EventRepository;
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

    public EventEntryResponse requestEntry(EventEntryRequest req) {
        EventEntry entry = new EventEntry();
        entry.setEventId(req.getEventId());
        entry.setUserId(req.getUserId());
        entry.setRequestedAt(LocalDateTime.now());
        repo.save(entry);

        EventEntryResponse res = new EventEntryResponse();
        res.setMessage("Entrada solicitada com sucesso!");
        return res;
    }

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
        
        // Versão modificada que filtra usuários não encontrados em vez de lançar exceção
        return entries.stream()
                      .map(entry -> {
                          try {
                              return userRepository.findById(entry.getUserId())
                                     .map(user -> new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt()))
                                     .orElse(null);
                          } catch (Exception e) {
                              System.out.println("Erro ao buscar usuário ID: " + entry.getUserId());
                              return null;
                          }
                      })
                      .filter(response -> response != null)
                      .collect(Collectors.toList());
    }
}