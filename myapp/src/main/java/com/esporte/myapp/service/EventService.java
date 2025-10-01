package com.esporte.myapp.service;

import com.esporte.myapp.dto.*;
import com.esporte.myapp.entity.Event;
import com.esporte.myapp.entity.EventEntry;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.enums.RequestStatus;
import com.esporte.myapp.repository.EventEntryRepository;
import com.esporte.myapp.repository.EventRepository;
import com.esporte.myapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository repo;
    private final SearchService searchService;
    private final UserRepository userRepo;
    private final EventEntryRepository eventEntryRepository;


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
                .filter(e -> filter.gender() == null || filter.gender().isBlank() || filter.gender().equalsIgnoreCase(e.getGender()))
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
        e.setCoverImageUrl(req.coverImageUrl());

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
    @Transactional(readOnly = true)
    public EventResponse getWithEvent(Long id) {
        Event e = repo.findByIdWithCreator(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        return toResponse(e,true);
    }

    private EventResponse toResponse(Event e){
        return toResponse(e, false);
    }

    // src/main/java/com/esporte/myapp/service/EventService.java
    private EventResponse toResponse(Event e, Boolean eager) {
        User creator = eager ? e.getCreator() : userRepo.findByid(e.getCreator().getId());

        String organizerId    = (creator != null ? creator.getId() : null);
        String organizerName  = (creator != null ? safe(creator.getName())  : null);
        String organizerPhoto = (creator != null ? safe(creator.getPhoto()) : null);

        // conta participantes aceitos (exclui organizador) e soma +1 do organizador
        long accepted = eventEntryRepository.countByEvent_IdAndStatus(
                e.getId(), com.esporte.myapp.enums.RequestStatus.ACCEPTED
        );
        int participantCount = (int) accepted + 1; // inclui o organizador

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
                organizerName,
                organizerPhoto,
                e.getCoverImageUrl(),
                e.getMaxParticipants(),
                participantCount
        );
    }


    public List<EventResponse> searchUpcomingByAnyField(String searchTerm) {
        return searchService.searchUpcomingByAnyField(searchTerm)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<UserEventItemResponse> getMyRegisteredEvents(String userId) {
        List<EventEntry> accepted = eventEntryRepository
                .findByUserIdAndStatusFetchEvent(userId, RequestStatus.ACCEPTED);

        List<Event> mineAsOrganizer = repo.findByCreator_Id(userId);

        LocalDate today = LocalDate.now();
        LocalTime now   = LocalTime.now();

        // concatena: (sou participante aceito) + (sou organizador)
        return java.util.stream.Stream.concat(
                        accepted.stream().map(EventEntry::getEvent),
                        mineAsOrganizer.stream()
                )
                // dedup por ID (como entidades JPA diferentes podem ser instâncias distintas)
                .collect(java.util.stream.Collectors.toMap(
                        Event::getId, e -> e, (a, b) -> a
                ))
                .values().stream()
                .filter(e -> isUpcoming(e, today, now))
                .sorted(Comparator
                        .comparing(Event::getDate, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(Event::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(UserEventItemResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserEventItemResponse> getMyParticipatedEvents(String userId) {
        List<EventEntry> accepted = eventEntryRepository
                .findByUserIdAndStatusFetchEvent(userId, RequestStatus.ACCEPTED);

        List<Event> mineAsOrganizer = repo.findByCreator_Id(userId);

        LocalDate today = LocalDate.now();
        LocalTime now   = LocalTime.now();

        return java.util.stream.Stream.concat(
                        accepted.stream().map(EventEntry::getEvent),
                        mineAsOrganizer.stream()
                )
                .collect(java.util.stream.Collectors.toMap(
                        Event::getId, e -> e, (a, b) -> a
                ))
                .values().stream()
                .filter(e -> isPast(e, today, now))
                .sorted(Comparator
                        .comparing(Event::getDate, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Event::getEndTime, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Event::getStartTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(UserEventItemResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public EventParticipantsResponse getParticipants(Long eventId, String currentUserId) {
        Event event = repo.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        var entries = eventEntryRepository.findAcceptedByEventIdFetchUser(eventId);

        // Monta os participantes (exclui o organizador)
        var participants = entries.stream()
                .filter(ee -> ee.getUser() != null
                        && (event.getCreator() == null || !ee.getUser().getId().equals(event.getCreator().getId())))
                .map(ee -> {
                    var u = ee.getUser();
                    // nome: atual do User; fallback para snapshot gravado na entry; fallback para prefixo do email
                    String name = firstNonBlank(
                            safe(u.getName()),
                            safe(ee.getRequesterName()),
                            (u.getEmail() != null ? u.getEmail().split("@")[0] : null)
                    );

                    // foto: atual do User; fallback para snapshot gravado na entry
                    String photo = firstNonBlank(
                            safe(u.getPhoto()),
                            safe(ee.getRequesterPhoto())
                    );

                    return new ParticipantDTO(u.getId(), name, photo);
                })
                .toList();

        long acceptedCount = eventEntryRepository.countByEvent_IdAndStatus(eventId, RequestStatus.ACCEPTED);
        Integer max = event.getMaxParticipants(); // pode ser null
        boolean iAmParticipant = false;
        if (currentUserId != null) {
            iAmParticipant = entries.stream().anyMatch(ee -> {
                var u = ee.getUser();
                return u != null && currentUserId.equals(u.getId());
            });
        }
        return new EventParticipantsResponse(participants, max, acceptedCount, iAmParticipant);
    }

    @Transactional(readOnly = true)
    public List<UserEventItemResponse> getCreatedBy(String userId) {
        return repo.findByCreator_Id(userId)
                .stream()
                .sorted(Comparator
                        .comparing(Event::getDate, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Event::getEndTime, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Event::getStartTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(UserEventItemResponse::from)
                .toList();
    }


    // ——— helpers ———

    // “ainda não aconteceu”: (date > hoje) ou (date == hoje e startTime >= agora ou startTime null)
    private boolean isUpcoming(Event e, LocalDate today, LocalTime now) {
        if (e.getDate() == null) return false;
        if (e.getDate().isAfter(today)) return true;
        if (e.getDate().isEqual(today)) {
            LocalTime start = e.getStartTime();
            System.out.println( e.getName() +" " + !start.isBefore(now));
            return (start == null) || !start.isBefore(now);
        }
        return false;
    }

    // “já aconteceu”: (date < hoje) ou (date == hoje e fim < agora) ou (sem fim e início < agora)
    private boolean isPast(Event e, LocalDate today, LocalTime now) {
        if (e.getDate() == null) return false;
        if (e.getDate().isBefore(today)) return true;
        if (e.getDate().isEqual(today)) {
            LocalTime end = e.getEndTime();
            LocalTime start = e.getStartTime();
            if (end != null) return end.isBefore(now);
            return start != null && start.isBefore(now);
        }
        return false;
    }
    private static String safe(String s) { return (s != null && !s.isBlank()) ? s : null; }
    private static String firstNonBlank(String... vals) {
        for (String v : vals) if (v != null && !v.isBlank()) return v;
        return null;
    }
}