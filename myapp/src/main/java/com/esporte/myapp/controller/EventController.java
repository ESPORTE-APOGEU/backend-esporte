package com.esporte.myapp.controller;

import com.esporte.myapp.dto.EventRequest;
import com.esporte.myapp.dto.EventResponse;
import com.esporte.myapp.dto.EventFilterRequest;
import com.esporte.myapp.service.EventService;
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

    private final EventService service;

    @GetMapping
    public java.util.List<EventResponse> getAllUpcoming() {
        return service.getUpcomingEvents();
    }

    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(req));
    }

    @PostMapping("/filter")
    public java.util.List<EventResponse> filter(@RequestBody EventFilterRequest filter) {
        return service.filter(filter);
    }

    @GetMapping("/{id}")
    public EventResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/search")
    public List<EventResponse> searchUpcoming(@RequestParam("q") String query) {
        return service.searchUpcomingByAnyField(query);
    }

}
