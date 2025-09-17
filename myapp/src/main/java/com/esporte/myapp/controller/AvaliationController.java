package com.esporte.myapp.controller;
import java.util.List;
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

        @GetMapping("/pending")
        public ResponseEntity<List<com.esporte.myapp.dto.AvaliationPendingResponse>> getPendingAvaliations(
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.oauth2.jwt.Jwt jwt,
            org.springframework.security.core.Authentication authentication
        ) {
            String userId = (jwt != null) ? jwt.getSubject() : null;
            if (userId == null && authentication != null) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof String s) userId = s; else userId = authentication.getName();
            }
            if (userId == null) {
                return ResponseEntity.status(401).build();
            }
            List<Avaliation> pending = avaliationRepository.findByToUserIdAndStatus(userId, Avaliation.Status.PENDING);
            List<com.esporte.myapp.dto.AvaliationPendingResponse> response = pending.stream()
                .map(a -> new com.esporte.myapp.dto.AvaliationPendingResponse(
                    a.getId(),
                    a.getToUser().getName(),
                    a.getToUser().getPhoto()
                ))
                .toList();
            return ResponseEntity.ok(response);
        }
    // Respond to an existing avaliation by id
    @PostMapping("/{id}/respond")
    @Transactional
    public ResponseEntity<Void> respond(@PathVariable Long id, @RequestBody AvaliationResponseRequest req,
                                        @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.oauth2.jwt.Jwt jwt,
                                        org.springframework.security.core.Authentication authentication) {
        Avaliation a = avaliationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Avaliation not found"));

        // Identifica o usuário autenticado
        String clerkId = (jwt != null) ? jwt.getSubject() : null;
        if (clerkId == null && authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof String s) clerkId = s; else clerkId = authentication.getName();
        }
        if (clerkId == null) {
            return ResponseEntity.status(401).build();
        }

        // Garante que o usuário autenticado é o autor da avaliação
        // Garante que o usuário autenticado é o destinatário da avaliação (quem deve responder)
        if (!a.getToUser().getId().equals(clerkId)) {
            return ResponseEntity.status(403).build();
        }

        // validação do rating
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

        // atualiza agregados do usuário
        avaliationService.updateUserAggregatesOnNewCompletion(a.getToUser().getId(), a.getRating(), skillNumeric);

        return ResponseEntity.noContent().build();
    }

    // Alternative endpoint: submit by event+from+to
    @PostMapping("/submit")
    @Transactional
    public ResponseEntity<Void> submit(@RequestParam Long eventId, @RequestParam String toUserId, @RequestBody AvaliationResponseRequest req,
                                       @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.oauth2.jwt.Jwt jwt,
                                       org.springframework.security.core.Authentication authentication) {
        // Identifica o usuário autenticado
        String clerkId = (jwt != null) ? jwt.getSubject() : null;
        if (clerkId == null && authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof String s) clerkId = s; else clerkId = authentication.getName();
        }
    if (clerkId == null) {
        return ResponseEntity.status(401).build();
    }

    final String authUserId = clerkId; // make effectively final for lambda usage

    Optional<Avaliation> found = avaliationRepository.findByEventAndFromUserAndToUser(
        avaliationService.getEventReferenceById(eventId),
        avaliationService.getUserReferenceById(authUserId),
        avaliationService.getUserReferenceById(toUserId)
    );

        Avaliation a = found.orElseGet(() -> {
            Avaliation n = new Avaliation();
            n.setEvent(avaliationService.getEventReferenceById(eventId));
            n.setFromUser(avaliationService.getUserReferenceById(authUserId));
            n.setToUser(avaliationService.getUserReferenceById(toUserId));
            n.setStatus(Avaliation.Status.PENDING);
            n.setRequestedAt(LocalDateTime.now());
            return avaliationRepository.save(n);
        });

        // Reutiliza lógica do respond
        return respond(a.getId(), req, jwt, authentication);
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
