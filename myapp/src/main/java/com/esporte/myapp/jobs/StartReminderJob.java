// StartReminderJob.java
package com.esporte.myapp.jobs;

import com.esporte.myapp.entity.Event;
import com.esporte.myapp.entity.EventEntry;
import com.esporte.myapp.enums.RequestStatus;
import com.esporte.myapp.repository.EventEntryRepository;
import com.esporte.myapp.repository.EventRepository;
import com.esporte.myapp.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StartReminderJob {

    private final EventRepository eventRepo;
    private final EventEntryRepository entryRepo;
    private final NotificationService notificationService;

    /**
     * Roda a cada 5 minutos.
     * Ajuste conforme necessário.
     */
    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void run() {
        LocalDateTime now = LocalDateTime.now();

        // Janela alvo: entre +2h e +2h+5m
        LocalDateTime windowStart = now.plusHours(2);
        LocalDateTime windowEnd   = now.plusHours(2).plusMinutes(5);

        // Buscamos eventos hoje/amanhã sem lembrete enviado
        LocalDate fromDate = windowStart.toLocalDate();
        LocalDate toDate   = windowEnd.toLocalDate();
        List<Event> candidates = eventRepo.findCandidatesForStartReminder(fromDate, toDate);

        for (Event e : candidates) {
            if (e.getDate() == null || e.getStartTime() == null) continue;

            LocalDateTime eventStart = LocalDateTime.of(e.getDate(), e.getStartTime());

            // filtra estritamente a janela alvo
            boolean inWindow =
                    ( !eventStart.isBefore(windowStart) ) && eventStart.isBefore(windowEnd);

            if (!inWindow) continue;

            // Coleta destinatários: organizador + aceitos
            Set<String> targetIds = new HashSet<>();
            if (e.getCreator() != null && e.getCreator().getId() != null) {
                targetIds.add(e.getCreator().getId());
            }

            List<EventEntry> accepted = entryRepo.findAcceptedByEventIdFetchUser(e.getId());
            for (EventEntry ee : accepted) {
                if (ee.getUser() != null && ee.getUser().getId() != null) {
                    targetIds.add(ee.getUser().getId());
                }
            }

            // Notifica todo mundo
            String title = "O evento já vai começar!";
            String description = e.getName() + " começa em 2 horas! Você está pronto?!";
            for (String uid : targetIds) {
                notificationService.notifyUser(
                        uid,
                        "event_start_reminder",
                        title,
                        description,
                        "calendar", // ícone que o app já usa
                        null,
                        null,
                        e.getId(),
                        null,
                        null
                );
            }

            // Marca como enviado (idempotência)
            e.setStartReminderSentAt(now);
            eventRepo.save(e);
        }
    }
}
