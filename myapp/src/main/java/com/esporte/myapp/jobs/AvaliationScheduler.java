package com.esporte.myapp.scheduler;

import com.esporte.myapp.entity.Event;
import com.esporte.myapp.repository.EventRepository;
import com.esporte.myapp.service.AvaliationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class AvaliationScheduler {

    private final EventRepository eventRepository;
    private final AvaliationService avaliationService;

    @Scheduled(fixedDelayString = "PT24H")
    @Transactional
    public void scanAndGenerate() {
        LocalDateTime now = LocalDateTime.now();
        List<Event> events = eventRepository.findAll();
        for (Event e : events) {
            try {
                LocalDateTime end = LocalDateTime.of(e.getDate(), e.getEndTime());
                if (end.isBefore(now) && (e.getAvaliationsRequested() == null || !e.getAvaliationsRequested())) {
                    avaliationService.generateRequestsForEvent(e.getId());
                    e.setAvaliationsRequested(true);
                    eventRepository.save(e);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}