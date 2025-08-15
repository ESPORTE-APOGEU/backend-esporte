package com.esporte.myapp;

import static org.mockito.Mockito.*;

import com.esporte.myapp.dto.EventEntryRequest;
import com.esporte.myapp.dto.EventEntryResponse;


import static org.junit.jupiter.api.Assertions.*;

import com.esporte.myapp.dto.UserResponse;
import com.esporte.myapp.entity.Event;
import com.esporte.myapp.entity.EventEntry;
import com.esporte.myapp.entity.Notification;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.EventEntryRepository;
import com.esporte.myapp.repository.EventRepository;
import com.esporte.myapp.repository.NotificationRepository;
import com.esporte.myapp.repository.UserRepository;
import com.esporte.myapp.service.EventEntryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class EventEntryServiceTest {

    @Mock
    private EventEntryRepository entryRepo;

    @Mock
    private EventRepository eventRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private NotificationRepository notificationRepo;

    @InjectMocks
    private EventEntryService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRequestEntryCreatesEntryAndNotification() {
        EventEntryRequest req = new EventEntryRequest();
        req.setEventId(100L);
        req.setUserId(200L);

        User participant = new User();
        participant.setId(200L);
        User organizer = new User();
        organizer.setId(300L);
        Event event = new Event();
        event.setId(100L);
        event.setName("Teste");
        event.setOrganizerId(300L);

        when(userRepo.findById(200L)).thenReturn(Optional.of(participant));
        when(entryRepo.save(any(EventEntry.class))).thenAnswer(inv -> inv.getArgument(0));
        when(eventRepo.findById(100L)).thenReturn(Optional.of(event));
        when(userRepo.findById(300L)).thenReturn(Optional.of(organizer));
        when(notificationRepo.save(any(Notification.class))).thenAnswer(inv -> inv.getArgument(0));

        EventEntryResponse resp = service.requestEntry(req);

        assertNotNull(resp);
        assertTrue(resp.getMessage().toLowerCase().contains("aguarde a resposta"));

        ArgumentCaptor<Notification> cap = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepo, times(1)).save(cap.capture());
        Notification notif = cap.getValue();
        assertEquals("entry_request", notif.getType());
        assertTrue(notif.getDescription().contains("Teste"));
    }

    @Test
    void testRequestEntryUserNotFoundThrows() {
        EventEntryRequest req = new EventEntryRequest();
        req.setEventId(1L);
        req.setUserId(99L);
        when(userRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.requestEntry(req));
        verify(entryRepo, never()).save(any());
        verify(notificationRepo, never()).save(any());
    }

    @Test
    void testAcceptEntryCreatesAcceptedNotification() {
        Long entryId = 10L;
        EventEntry entry = new EventEntry();
        entry.setId(entryId);
        User participant = new User();
        participant.setId(200L);
        entry.setUser(participant);
        entry.setEventId(50L);

        Event event = new Event();
        event.setId(50L);
        event.setName("EventoX");

        when(entryRepo.findById(entryId)).thenReturn(Optional.of(entry));
        when(entryRepo.save(any(EventEntry.class))).thenAnswer(inv -> inv.getArgument(0));
        when(eventRepo.findById(50L)).thenReturn(Optional.of(event));
        when(notificationRepo.save(any(Notification.class))).thenAnswer(inv -> inv.getArgument(0));

        service.acceptEntry(entryId);

        verify(entryRepo, times(1)).save(entry);
        ArgumentCaptor<Notification> cap = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepo, times(1)).save(cap.capture());
        Notification notif = cap.getValue();
        assertEquals("entry_accepted", notif.getType());
        assertTrue(notif.getDescription().contains("EventoX"));
    }

    @Test
    void testGetParticipantsReturnsUserResponses() {
        Long eventId = 5L;
        User user1 = new User(); user1.setId(101L); user1.setName("Alice"); user1.setEmail("a@x.com"); user1.setCreatedAt(LocalDateTime.now());
        User user2 = new User(); user2.setId(102L); user2.setName("Bob");   user2.setEmail("b@x.com"); user2.setCreatedAt(LocalDateTime.now());

        EventEntry e1 = new EventEntry(); e1.setUser(user1);
        EventEntry e2 = new EventEntry(); e2.setUser(user2);

        when(entryRepo.findByEventId(eventId)).thenReturn(Arrays.asList(e1, e2));

        List<UserResponse> participants = service.getParticipants(eventId);

        assertEquals(2, participants.size());
        assertEquals(101L, participants.get(0).id());
        assertEquals("Alice", participants.get(0).name());
        assertEquals(102L, participants.get(1).id());
        assertEquals("Bob", participants.get(1).name());
    }
}
