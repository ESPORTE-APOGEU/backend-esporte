package com.esporte.myapp.service;

import com.esporte.myapp.dto.EventRequest;
import com.esporte.myapp.dto.EventResponse;
import com.esporte.myapp.dto.EventFilterRequest;
import com.esporte.myapp.entity.Event;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.EventRepository;
import com.esporte.myapp.repository.UserRepository;
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
    private final UserRepository userRepo;


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

    public EventResponse create(EventRequest req, String clerkId) {
        var creator = userRepo.findById(clerkId)
                .orElseThrow(() -> new IllegalStateException("Usuário (creator) não encontrado"));

        Event e = new Event();
        e.setCreator(creator);
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
        e.setWhatsappLink(req.whatsappLink());
        e.setPrivate(req.isPrivate());
        e.setMinParticipants(req.minParticipants());
        e.setMaxParticipants(req.maxParticipants());

        // Só cria o ponto se vier latitude/longitude
        if (req.latitude() != null && req.longitude() != null) {
            GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
            // Ordem correta: (lon, lat)
            Point point = gf.createPoint(new Coordinate(req.longitude(), req.latitude()));
            e.setLocationPoint(point);
        } else {
            e.setLocationPoint(null);
        }

        e = repo.save(e);
        return toResponse(e);
    }

    public EventResponse get(Long id) {
        Event e = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        return toResponse(e);
    }

    private EventResponse toResponse(Event e) {
        User creator = userRepo.findByid(e.getCreator().getId());

        // Ajuste estes getters para o que existir na sua entidade User
        // (ex.: getFullName()/getName(), getPhotoUrl()/getAvatar(), etc.)
        String organizerId    = creator != null ? creator.getId() : null; // se seu ID for String, ok; senão toString()
        String organizerName  = null;
        String organizerPhoto = null;
        if (creator != null) {
            // tente primeiro um "full name" e caia para "name" se necessário
            try {
                organizerName = creator.getName();
            } catch (Exception ignored) {}
            if (organizerName == null) {
                try { organizerName = creator.getName(); } catch (Exception ignored) {}
            }

        }

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
                e.getDescription(),
                organizerId,
                organizerName
        );
    }


    public List<EventResponse> searchUpcomingByAnyField(String searchTerm) {
        return searchService.searchUpcomingByAnyField(searchTerm)
                .stream()
                .map(this::toResponse)
                .toList();
    }

}