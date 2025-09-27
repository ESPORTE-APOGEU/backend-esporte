package com.esporte.myapp.service;

import com.esporte.myapp.dto.EventResponse;
import com.esporte.myapp.entity.Event;
import com.esporte.myapp.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor

public class SearchService {
    private final EventRepository repo;

    public List<Event> searchUpcoming(String searchTerm) {
        LocalDate today = LocalDate.now();

        Map<Long, Event> byId = new LinkedHashMap<>();

        repo.findByDateGreaterThanEqualAndNameContainingIgnoreCase(today, searchTerm)
                .forEach(e -> byId.putIfAbsent(e.getId(), e));

        repo.findByDateGreaterThanEqualAndSportContainingIgnoreCase(today, searchTerm)
                .forEach(e -> byId.putIfAbsent(e.getId(), e));

        repo.findByDateGreaterThanEqualAndDescriptionContainingIgnoreCase(today, searchTerm)
                .forEach(e -> byId.putIfAbsent(e.getId(), e));

        // novo: nome do organizador
        repo.findUpcomingByCreatorName(today, searchTerm)
                .forEach(e -> byId.putIfAbsent(e.getId(), e));

        return new ArrayList<>(byId.values());
    }

    public List<Event> searchUpcomingByAnyField(String searchTerm) {
        return searchUpcoming(searchTerm);
    }
}