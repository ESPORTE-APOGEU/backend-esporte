package com.esporte.myapp.service;

import com.esporte.myapp.dto.EventResponse;
import com.esporte.myapp.entity.Event;
import com.esporte.myapp.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class SearchService {
    private final EventRepository repo;

    public List<Event> searchUpcoming(String searchTerm) {
        LocalDate today = LocalDate.now();

        Set<Event> results = new LinkedHashSet<>();
        results.addAll(repo.findByDateGreaterThanEqualAndNameContainingIgnoreCase(today, searchTerm));
//        results.addAll(repo.findByDateGreaterThanEqualAndCreator_NameContainingIgnoreCase(today, searchTerm));
        results.addAll(repo.findByDateGreaterThanEqualAndSportContainingIgnoreCase(today, searchTerm));
        results.addAll(repo.findByDateGreaterThanEqualAndDescriptionContainingIgnoreCase(today, searchTerm));

        return new ArrayList<>(results);
    }

    public List<Event> searchUpcomingByAnyField(String searchTerm) {
        return searchUpcoming(searchTerm);
    }

}
