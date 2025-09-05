package com.esporte.myapp.controller;

import com.esporte.myapp.dto.AvaliationResponseRequest;
import com.esporte.myapp.entity.Avaliation;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.AvaliationRepository;
import com.esporte.myapp.repository.UserRepository;
import com.esporte.myapp.service.AvaliationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/avaliations")
@RequiredArgsConstructor
public class AvaliationController {

    private final AvaliationRepository avaliationRepository;
    private final UserRepository userRepository;
    private final AvaliationService avaliationService;

    // Respond to an existing avaliation by id
    @PostMapping("/{id}/respond")
    @Transactional
    public ResponseEntity<Void> respond(@PathVariable Long id, @RequestBody AvaliationResponseRequest req) {
        Avaliation a = avaliationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Avaliation not found"));

        // Verify fromUser matches
        User from = userRepository.findById(req.fromUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!a.getFromUser().getId().equals(from.getId())) {
            return ResponseEntity.status(403).build();
        }

        // validate rating
        if (req.rating() != null) {
            if (req.rating() < 1 || req.rating() > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5");
            }
            a.setRating(req.rating());
        }

        a.setComment(req.comment());

        Integer skillNumeric = null;
        if (req.skillLevel() != null) {
            try {
                Avaliation.SkillLevel enumVal = Avaliation.SkillLevel.valueOf(req.skillLevel());
                a.setSkillLevel(enumVal);
                skillNumeric = convertSkillToInt(enumVal);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Invalid skill level");
            }
        }

        a.setStatus(Avaliation.Status.COMPLETED);
        a.setRespondedAt(LocalDateTime.now());

        avaliationRepository.save(a);

        // update user's aggregated fields (pass rating and numeric skill)
        avaliationService.updateUserAggregatesOnNewCompletion(a.getToUser().getId(), a.getRating(), skillNumeric);

        return ResponseEntity.noContent().build();
    }

    // Alternative endpoint: submit by event+from+to
    @PostMapping("/submit")
    @Transactional
    public ResponseEntity<Void> submit(@RequestParam Long eventId, @RequestParam Long fromUserId, @RequestParam Long toUserId, @RequestBody AvaliationResponseRequest req) {
        Optional<Avaliation> found = avaliationRepository.findByEventAndFromUserAndToUser(
                avaliationService.getEventReferenceById(eventId),
                avaliationService.getUserReferenceById(fromUserId),
                avaliationService.getUserReferenceById(toUserId)
        );

        Avaliation a = found.orElseGet(() -> {
            Avaliation n = new Avaliation();
            n.setEvent(avaliationService.getEventReferenceById(eventId));
            n.setFromUser(avaliationService.getUserReferenceById(fromUserId));
            n.setToUser(avaliationService.getUserReferenceById(toUserId));
            n.setStatus(Avaliation.Status.PENDING);
            n.setRequestedAt(LocalDateTime.now());
            return avaliationRepository.save(n);
        });

        // reuse respond logic
        AvaliationResponseRequest r = req;
        return respond(a.getId(), r);
    }

    private int convertSkillToInt(Avaliation.SkillLevel s) {
        return switch (s) {
            case INICIANTE -> 0;
            case INTERMEDIARIO -> 1;
            case AVANCADO -> 2;
            case SEMIPROFISSIONAL -> 3;
        };
    }
}
