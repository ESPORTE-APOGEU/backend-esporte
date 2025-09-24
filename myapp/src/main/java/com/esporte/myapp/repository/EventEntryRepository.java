package com.esporte.myapp.repository;

import com.esporte.myapp.entity.EventEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventEntryRepository extends JpaRepository<EventEntry, Long> {
    List<EventEntry> findByEventId(Long eventId);

    List<EventEntry> findByEventIdAndStatus(Long id, String string);

    // Carrega o usuário junto para evitar LazyInitializationException
    @Query("select e from EventEntry e join fetch e.user u where e.eventId = :eventId and e.status = :status")
    List<EventEntry> findByEventIdAndStatusFetchUser(@Param("eventId") Long eventId, @Param("status") String status);
}