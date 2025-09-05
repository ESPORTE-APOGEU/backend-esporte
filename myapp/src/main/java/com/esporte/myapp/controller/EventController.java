// src/main/java/com/esporte/myapp/controller/EventController.java
package com.esporte.myapp.controller;

import com.esporte.myapp.dto.EventRequest;
import com.esporte.myapp.dto.EventResponse;
import com.esporte.myapp.service.AvaliationService;
import com.esporte.myapp.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;
    private final AvaliationService avaliationService;

    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(req));
    }

    @GetMapping("/{id}")
    public EventResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping("/{id}/avaliations/request")
    public ResponseEntity<Void> requestAvaliations(@PathVariable Long id) {
        avaliationService.generateRequestsForEvent(id);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{id}/participants/{userId}")
    public ResponseEntity<Void> addParticipant(@PathVariable Long id, @PathVariable Long userId) {
        service.addParticipant(id, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}/participants/{userId}")
    public ResponseEntity<Void> removeParticipant(@PathVariable Long id, @PathVariable Long userId) {
        service.removeParticipant(id, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
