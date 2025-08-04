package com.esporte.myapp.repository;

import com.esporte.myapp.entity.EventEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventEntryRepository extends JpaRepository<EventEntry, Long> {
    List<EventEntry> findByEventId(Long eventId);
}