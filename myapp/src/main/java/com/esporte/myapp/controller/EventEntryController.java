package com.esporte.myapp.controller;

import com.esporte.myapp.dto.EventEntryRequest;
import com.esporte.myapp.dto.EventEntryResponse;
import com.esporte.myapp.service.EventEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/event-entries")
@RequiredArgsConstructor
public class EventEntryController {

    private final EventEntryService service;

    @PostMapping
    public ResponseEntity<EventEntryResponse> requestEntry(@RequestBody EventEntryRequest req) {
        EventEntryResponse res = service.requestEntry(req);
        return ResponseEntity.ok(res);
    }

    // Endpoint para aceitar um pedido de entrada
    @PostMapping("/{entryId}/accept")
    public ResponseEntity<Object> acceptEntry(@PathVariable Long entryId) {
        service.acceptEntry(entryId);
        return ResponseEntity.status(HttpStatus.OK).body("Pedido aceito com sucesso!");
    }
    
    // Endpoint para recusar pode ser criado de forma similar
}