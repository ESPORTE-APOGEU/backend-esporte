package com.esporte.myapp.service;

import com.esporte.myapp.dto.EventRequest;
import com.esporte.myapp.dto.EventResponse;
import com.esporte.myapp.dto.EventFilterRequest;
import com.esporte.myapp.entity.Event;
import com.esporte.myapp.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository repo;
    private final SearchService searchService;

    public List<EventResponse> getUpcomingEvents() {
        java.time.LocalDate today = java.time.LocalDate.now();
        return repo.findAll().stream()
            .filter(e -> e.getDate().isAfter(today) || e.getDate().isEqual(today))
            .map(this::toResponse)
            .toList();
    }

    public List<EventResponse> filter(EventFilterRequest filter) {
        java.time.LocalDate today = java.time.LocalDate.now();
        List<Event> base = filter.latitude() != null && filter.longitude() != null && filter.maxDistanceKm() != null
                ? repo.findWithinRadius(filter.latitude(), filter.longitude(), filter.maxDistanceKm() * 1000)
                : repo.findAll();

        return base.stream()
                .filter(e -> filter.name() == null || e.getName().toLowerCase().contains(filter.name().toLowerCase()))
                .filter(e -> e.getDate().isAfter(today) || e.getDate().isEqual(today))
                .filter(e -> filter.sports() == null || filter.sports().isEmpty() || filter.sports().contains(e.getSport()))
                .filter(e -> filter.levels() == null || filter.levels().isEmpty() || filter.levels().contains(e.getLevel()))
                .filter(e -> filter.date() == null || e.getDate().equals(filter.date()))
                .filter(e -> {
                    if (filter.startTime() != null && filter.endTime() != null) {
                        return !(e.getEndTime().isBefore(filter.startTime()) || e.getStartTime().isAfter(filter.endTime()));
                    }
                    return true;
                })
                .map(this::toResponse)
                .toList();
    }

    public EventResponse create(EventRequest req) {
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = gf.createPoint(new Coordinate(req.longitude(), req.latitude()));
        Event e = new Event();
        e.setName(req.name());
        e.setLocation(req.location());
        e.setSport(req.sport());
        e.setLevel(req.level());
        e.setGender(req.gender());
        e.setDate(req.date());
        e.setStartTime(req.startTime());
        e.setEndTime(req.endTime());
        e.setPrice(req.price());
        e.setDescription(req.description());
        e.setLocationPoint(point);
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

    public List<EventResponse> searchUpcomingByAnyField(String searchTerm) {
        return searchService.searchUpcomingByAnyField(searchTerm)
                .stream()
                .map(this::toResponse)
                .toList();
    }

}