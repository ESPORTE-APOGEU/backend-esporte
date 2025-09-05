// src/main/java/com/esporte/myapp/controller/EventController.java
package com.esporte.myapp.controller;

import com.esporte.myapp.dto.EventRequest;
import com.esporte.myapp.dto.EventResponse;
<<<<<<< HEAD
import com.esporte.myapp.dto.UserResponse;
import com.esporte.myapp.service.EventEntryService;
=======
import com.esporte.myapp.service.EventService;
>>>>>>> parent of 54d1e99 (Merge branch 'dev' into origin/feat/back-amizade)
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

<<<<<<< HEAD
    private final EventEntryService service;
=======
    private final EventService service;
>>>>>>> parent of 54d1e99 (Merge branch 'dev' into origin/feat/back-amizade)

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
<<<<<<< HEAD

    // Lista todos os eventos
    @GetMapping
    public List<EventResponse> listAll() {
        return service.listAll();
    }

    // Agora retorna uma lista de UserResponse
    @GetMapping("/{id}/participants")
    public List<UserResponse> getParticipants(@PathVariable("id") Long id) {
        return service.getParticipants(id);
    }
=======
>>>>>>> parent of 54d1e99 (Merge branch 'dev' into origin/feat/back-amizade)
}
