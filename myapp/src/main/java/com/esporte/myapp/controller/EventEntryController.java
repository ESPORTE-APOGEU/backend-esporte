package com.esporte.myapp.controller;

import com.esporte.myapp.dto.EventEntryRequest;
import com.esporte.myapp.dto.EventEntryResponse;
import com.esporte.myapp.service.EventEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/event-entries")
@RequiredArgsConstructor
public class EventEntryController {

    private final EventEntryService service;

    @PostMapping
    public ResponseEntity<EventEntryResponse> requestEntry(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody EventEntryRequest req
    ) {
        String userId = (jwt != null) ? jwt.getSubject() : null;
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String nameFromJwt =
                firstNonBlank(
                        jwt.getClaimAsString("full_name"),
                        jwt.getClaimAsString("name"),
                        jwt.getClaimAsString("first_name")
                );
        String email = jwt.getClaimAsString("email");
        if (!hasText(nameFromJwt) && hasText(email)) {
            nameFromJwt = email.split("@")[0];
        }

        String photoFromJwt =
                firstNonBlank(
                        jwt.getClaimAsString("picture"),
                        jwt.getClaimAsString("image_url"),
                        jwt.getClaimAsString("avatar")
                );

        EventEntryResponse res = service.requestEntry(userId, req.eventId(), nameFromJwt, photoFromJwt);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // Endpoint para aceitar um pedido de entrada
    @PostMapping("/{entryId}/accept")
    public ResponseEntity<String> acceptEntry(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long entryId
    ) {
        String organizerId = (jwt != null) ? jwt.getSubject() : null;
        if (organizerId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        service.acceptEntry(entryId, organizerId);
        return ResponseEntity.ok("Pedido aceito com sucesso!");
    }

    @PostMapping("/{entryId}/decline")
    public ResponseEntity<String> declineEntry(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long entryId
    ) {
        String organizerId = (jwt != null) ? jwt.getSubject() : null;
        if (organizerId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        service.declineEntry(entryId, organizerId);
        return ResponseEntity.ok("Pedido recusado com sucesso!");
    }



    @GetMapping("/me")
    public ResponseEntity<EventEntryResponse> myEntry(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam Long eventId
    ) {
        String userId = (jwt != null) ? jwt.getSubject() : null;
        if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<EventEntryResponse> res = service.getMyEntry(userId, eventId);
        return res.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    private static boolean hasText(String s) {
        return s != null && !s.isBlank();
    }
    private static String firstNonBlank(String... vals) {
        for (String v : vals) if (hasText(v)) return v;
        return null;
    }
}