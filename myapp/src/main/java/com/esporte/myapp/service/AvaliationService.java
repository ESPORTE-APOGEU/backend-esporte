package com.esporte.myapp.service;

import com.esporte.myapp.entity.Avaliation;
import com.esporte.myapp.entity.Event;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.AvaliationRepository;
import com.esporte.myapp.repository.EventRepository;
import com.esporte.myapp.repository.UserRepository;
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

    @Transactional
    public void generateRequestsForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        LocalDateTime eventEnd = LocalDateTime.of(event.getDate(), event.getEndTime());
        if (eventEnd.isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Event has not ended yet");
        }

        List<User> participants = new ArrayList<>(event.getParticipants());

        // Shuffle participants to create random 1:1 pairings
        Collections.shuffle(participants, new Random());

        int n = participants.size();
        // Pair neighbors (0-1, 2-3, ...). If odd, last participant is left out.
        for (int i = 0; i + 1 < n; i += 2) {
            User first = participants.get(i);
            User second = participants.get(i + 1);

            // create request first -> second if not exists
            boolean existsAtoB = avaliationRepository.findByEventAndFromUserAndToUser(event, first, second).isPresent();
            if (!existsAtoB) {
                Avaliation a = new Avaliation();
                a.setEvent(event);
                a.setFromUser(first);
                a.setToUser(second);
                a.setStatus(Avaliation.Status.PENDING);
                a.setRequestedAt(LocalDateTime.now());
                avaliationRepository.save(a);
            }

            // create request second -> first if not exists (so both receive one evaluation)
            boolean existsBtoA = avaliationRepository.findByEventAndFromUserAndToUser(event, second, first).isPresent();
            if (!existsBtoA) {
                Avaliation b = new Avaliation();
                b.setEvent(event);
                b.setFromUser(second);
                b.setToUser(first);
                b.setStatus(Avaliation.Status.PENDING);
                b.setRequestedAt(LocalDateTime.now());
                avaliationRepository.save(b);
            }
        }

        // If odd number of participants the last one (participants.get(n-1)) is left without pairing.
        // To form a trio instead, implement logic here to pair the last three participants appropriately.
    }

    public Event getEventReferenceById(String eventId) {
        try {
            Long id = Long.parseLong(eventId);
            return eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("EventId deve ser um número válido.");
        }
    }

    public User getUserReferenceById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional
    public void updateUserAggregatesOnNewCompletion(String toUserId, Integer rating, Integer skillLevel) {
        // Incrementally update user's aggregated counters instead of recomputing everything
        User user = userRepository.findById(toUserId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Integer currentSkillSum = user.getTotalSkill() == null ? 0 : user.getTotalSkill();
        Integer currentRatingSum = user.getTotalRating() == null ? 0 : user.getTotalRating();
        Integer currentCount = user.getTotalReceivedEvaluations() == null ? 0 : user.getTotalReceivedEvaluations();

        if (rating != null) {
            currentRatingSum += rating;
            user.setTotalRating(currentRatingSum);
        }
        if (skillLevel != null) {
            currentSkillSum += skillLevel;
            user.setTotalSkill(currentSkillSum);
        }
        currentCount += 1;
        user.setTotalReceivedEvaluations(currentCount);

        userRepository.save(user);
    }
}
