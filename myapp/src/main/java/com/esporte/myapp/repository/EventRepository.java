package com.esporte.myapp.repository;

import com.esporte.myapp.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    
    @Query("SELECT e FROM Event e WHERE FUNCTION('TIMESTAMP', e.date, e.startTime) BETWEEN :startDateTime AND :endDateTime")
    List<Event> findEventsStartingBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}