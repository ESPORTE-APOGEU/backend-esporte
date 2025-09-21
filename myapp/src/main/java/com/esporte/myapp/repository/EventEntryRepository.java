package com.esporte.myapp.repository;

import com.esporte.myapp.entity.EventEntry;
import com.esporte.myapp.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventEntryRepository extends JpaRepository<EventEntry, Long> {

    Optional<EventEntry> findByEvent_IdAndUser_Id(Long eventId, String userId);

    List<EventEntry> findByEvent_Id(Long eventId);

    List<EventEntry> findByEvent_IdAndStatus(Long eventId, RequestStatus status);

    long countByEvent_IdAndStatus(Long eventId, RequestStatus status);

    Optional<EventEntry> findFirstByUser_IdAndEvent_Id(String userId, Long eventId);

}
