package com.esporte.myapp.service;

import com.esporte.myapp.entity.Event;
import com.esporte.myapp.entity.EventEntry;
import com.esporte.myapp.entity.Notification;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.EventEntryRepository;
import com.esporte.myapp.repository.EventRepository;
import com.esporte.myapp.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationScheduler {
    private final EventRepository eventRepository;
    private final EventEntryRepository eventEntryRepository;
    private final NotificationRepository notificationRepository;

    // Executa a cada 5 minutos, por exemplo:
    @Scheduled(fixedRate = 300000)
    public void sendUpcomingEventNotifications() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoHoursLater = now.plusHours(2);

        // Buscar eventos começando entre now e twoHoursLater (certifique-se de ter o campo startDateTime ou adapte a lógica)
        List<Event> upcomingEvents = eventRepository.findEventsStartingBetween(now, twoHoursLater);
        for (Event event : upcomingEvents) {
            // (Opcional) Verificar se notificações já foram enviadas para evitar duplicidade

            // Buscar os participantes aceitos
            List<EventEntry> entries = eventEntryRepository.findByEventIdAndStatus(event.getId(), "ACCEPTED");
            for (EventEntry entry : entries) {
                User participant = entry.getUser();

                // Notificação 1: Lembrete do início do evento
                Notification reminder = new Notification();
                reminder.setUser(participant);
                reminder.setType("event_start_reminder");
                reminder.setIconName("calendar");
                reminder.setTitle("O evento já vai começar!");
                reminder.setDescription(event.getName() + " começa em 2 horas! Você está pronto?!");
                reminder.setTimestamp(LocalDateTime.now());
                reminder.setRelatedEventId(event.getId());
                notificationRepository.save(reminder);

                // Notificação 2: Informação de local do evento
                Notification locationNotif = new Notification();
                locationNotif.setUser(participant);
                locationNotif.setType("event_location");
                locationNotif.setIconName("info");
                locationNotif.setTitle("Local do evento");
                locationNotif.setDescription(event.getName() + " está localizado em " + event.getLocation());
                locationNotif.setTimestamp(LocalDateTime.now());
                locationNotif.setRelatedEventId(event.getId());
                notificationRepository.save(locationNotif);
            }
        }
    }
}