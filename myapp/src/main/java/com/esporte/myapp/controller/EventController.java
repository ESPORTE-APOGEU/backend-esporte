// src/main/java/com/esporte/myapp/controller/EventController.java
package com.esporte.myapp.controller;

import com.esporte.myapp.dto.EventRequest;
import com.esporte.myapp.dto.EventResponse;
import com.esporte.myapp.entity.EventParticipant;
import com.esporte.myapp.service.EventEntryService; // Corrigido o package import
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventEntryService service;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody EventRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(req));
    }

    @GetMapping("/{id}")
    public EventResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    // Lista todos os eventos
    @GetMapping
    public List<EventResponse> listAll() {
        return service.listAll();
    }

    // Lista participantes de um evento
    @GetMapping("/{id}/participants")
    public List<EventParticipant> getParticipants(@PathVariable("id") Long id) {
        return service.getParticipants(id);
    }
}
