package com.esporte.myapp.service;

import com.esporte.myapp.dto.EventEntryRequest;
import com.esporte.myapp.dto.EventEntryResponse;
import com.esporte.myapp.dto.EventRequest;
import com.esporte.myapp.dto.EventResponse;
import com.esporte.myapp.entity.EventEntry;
import com.esporte.myapp.repository.EventEntryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventEntryService {
    private final EventEntryRepository repo;

    public EventEntryResponse requestEntry(EventEntryRequest req) {
        // Se necessário, verifique se o usuário já solicitou entrada
        EventEntry entry = new EventEntry();
        entry.setEventId(req.getEventId());
        entry.setUserId(req.getUserId());
        entry.setRequestedAt(LocalDateTime.now());
        repo.save(entry);

        EventEntryResponse res = new EventEntryResponse();
        res.setMessage("Entrada solicitada com sucesso!");
        return res;
    }

    public Object create(EventRequest req) {
        
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    public EventResponse get(Long id) {
        
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }
}