// src/main/java/com/esporte/myapp/controller/EventController.java
package com.esporte.myapp.controller;

import com.esporte.myapp.dto.EventRequest;
import com.esporte.myapp.dto.EventResponse;
import com.esporte.myapp.service.EventEntryService; // Corrigido o package import
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
