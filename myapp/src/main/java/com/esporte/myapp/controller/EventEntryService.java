package com.esporte.myapp.controller;

import com.esporte.myapp.dto.EventRequest;
import com.esporte.myapp.dto.EventResponse;
import com.esporte.myapp.dto.UserResponse;
import com.esporte.myapp.service.EventService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventEntryService {
    private final EventService service;

    public List<UserResponse> getParticipants(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'getParticipants'");
    }

    public List<EventResponse> listAll() {
        throw new UnsupportedOperationException("Unimplemented method 'listAll'");
    }

    public EventResponse get(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    public Object create(EventRequest req) {
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    public EventService getService() {
        return service;
    }
}
