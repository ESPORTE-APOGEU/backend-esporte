package com.esporte.myapp.service;

import com.esporte.myapp.entity.*;
import com.esporte.myapp.enums.RequestStatus;
import com.esporte.myapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AvaliationService {

    private final EventRepository eventRepository;
    private final AvaliationRepository avaliationRepository;
    private final UserRepository userRepository;
    private final EventEntryRepository eventEntryRepository;
    private final UserSportStatsRepository userSportStatsRepository;

    // AvaliationService.java
    @Transactional
    public void generateRequestsForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        LocalDateTime eventEnd = LocalDateTime.of(event.getDate(), event.getEndTime());
        if (eventEnd.isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Event has not ended yet");
        }

        var entries = eventEntryRepository.findAcceptedByEventIdFetchUser(event.getId());

        // Participantes + ORGANIZADOR
        List<User> participants = new ArrayList<>(entries.stream().map(EventEntry::getUser).toList());
        User organizer = event.getCreator();
        if (organizer != null && participants.stream().noneMatch(u -> u.getId().equals(organizer.getId()))) {
            participants.add(organizer);
        }

        // Se menos de 2 pessoas, não gera nada
        if (participants.size() < 2) return;

        // Pareamento cíclico: u[i] avalia u[(i+1) % n]
        int n = participants.size();
        for (int i = 0; i < n; i++) {
            User from = participants.get(i);
            User to = participants.get((i + 1) % n);
            if (from.getId().equals(to.getId())) continue; // segurança

            boolean exists = avaliationRepository
                    .findByEventAndFromUserAndToUser(event, from, to)
                    .isPresent();

            if (!exists) {
                Avaliation a = new Avaliation();
                a.setEvent(event);
                a.setFromUser(from); // AVALIADOR
                a.setToUser(to);     // AVALIADO
                a.setSport(event.getSport());
                a.setStatus(Avaliation.Status.PENDING);
                a.setRequestedAt(LocalDateTime.now());
                avaliationRepository.save(a);
            }
        }
    }

    public Event getEventReferenceById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));
    }

    public User getUserReferenceById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional
    public void updateUserAggregatesOnNewCompletion(String toUserId, Integer rating, Integer skillLevel, String sport) {
        // ----- agregados globais -----
        User user = userRepository.findById(toUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Integer currentSkillSum = user.getTotalSkill() == null ? 0 : user.getTotalSkill();
        Integer currentRatingSum = user.getTotalRating() == null ? 0 : user.getTotalRating();
        Integer currentCount     = user.getTotalReceivedEvaluations() == null ? 0 : user.getTotalReceivedEvaluations();

        if (rating != null) {
            currentRatingSum += rating;
            user.setTotalRating(currentRatingSum);
        }
        if (skillLevel != null) {
            currentSkillSum += skillLevel;
            user.setTotalSkill(currentSkillSum);
        }
        user.setTotalReceivedEvaluations(currentCount + 1);
        userRepository.save(user);

        // ----- agregados por esporte -----
        if (sport != null && !sport.isBlank() && skillLevel != null) {
            UserSportStats stats = userSportStatsRepository
                    .findByUser_IdAndSport(toUserId, sport)
                    .orElseGet(() -> {
                        UserSportStats s = new UserSportStats();
                        s.setUser(user);
                        s.setSport(sport);
                        s.setTotalSkill(0);
                        s.setTotalReceivedEvaluations(0);
                        return s;
                    });

            stats.setTotalSkill(stats.getTotalSkill() + skillLevel);
            stats.setTotalReceivedEvaluations(stats.getTotalReceivedEvaluations() + 1);
            stats.setUpdatedAt(java.time.LocalDateTime.now());
            userSportStatsRepository.save(stats);
        }
    }
}