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
import lombok.RequiredArgsConstructor;
import com.esporte.myapp.repository.UserRepository;
import com.esporte.myapp.entity.User;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class SearchService {
    private final EventRepository repo;
    private final UserRepository userRepository;

    public List<Event> searchUpcoming(String searchTerm) {
        LocalDate today = LocalDate.now();

        Set<Event> results = new LinkedHashSet<>();

        // support searching by username when query starts with '@'
        if (searchTerm != null && searchTerm.startsWith("@")) {
            String name = searchTerm.substring(1);
            if (name.isBlank()) return new ArrayList<>();

            // Busca eventos criados pelo usuário (não participantes)
            results.addAll(repo.findByDateGreaterThanEqualAndCreator_NameContainingIgnoreCase(today, name));
            return new ArrayList<>(results);
        }

        results.addAll(repo.findByDateGreaterThanEqualAndNameContainingIgnoreCase(today, searchTerm));
    // results.addAll(repo.findByDateGreaterThanEqualAndCreator_NameContainingIgnoreCase(today, searchTerm));
        results.addAll(repo.findByDateGreaterThanEqualAndSportContainingIgnoreCase(today, searchTerm));
        results.addAll(repo.findByDateGreaterThanEqualAndDescriptionContainingIgnoreCase(today, searchTerm));

        return new ArrayList<>(results);
    }

    public List<Event> searchUpcomingByAnyField(String searchTerm) {
        return searchUpcoming(searchTerm);
    }

}
