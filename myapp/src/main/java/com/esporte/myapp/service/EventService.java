package com.esporte.myapp.service;

import com.esporte.myapp.dto.EventRequest;
import com.esporte.myapp.dto.EventResponse;
import com.esporte.myapp.entity.Event;
import com.esporte.myapp.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository repo;

    public EventResponse create(EventRequest req) {
        Event e = new Event(
                null,
                req.name(),
                req.location(),
                req.sport(),
                req.level(),
                req.gender(),
                req.date(),
                req.startTime(),
                req.endTime(),
                req.price(),
                req.description()
        );
        e = repo.save(e);
        return toResponse(e);
    }

    public EventResponse get(Long id) {
        Event e = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        return toResponse(e);
    }

    private EventResponse toResponse(Event e) {
        return new EventResponse(
                e.getId(),
                e.getName(),
                e.getLocation(),
                e.getSport(),
                e.getLevel(),
                e.getGender(),
                e.getDate(),
                e.getStartTime(),
                e.getEndTime(),
                e.getPrice(),
                e.getDescription()
        );
    }
}