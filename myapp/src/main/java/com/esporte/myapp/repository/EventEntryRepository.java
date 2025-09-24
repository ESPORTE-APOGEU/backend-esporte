package com.esporte.myapp.repository;

import com.esporte.myapp.entity.EventEntry;
import com.esporte.myapp.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventEntryRepository extends JpaRepository<EventEntry, Long> {

    Optional<EventEntry> findByEvent_IdAndUser_Id(Long eventId, String userId);

    List<EventEntry> findByEvent_Id(Long eventId);

    List<EventEntry> findByEvent_IdAndStatus(Long eventId, RequestStatus status);

    long countByEvent_IdAndStatus(Long eventId, RequestStatus status);

    Optional<EventEntry> findFirstByUser_IdAndEvent_Id(String userId, Long eventId);

    @Query("""
           select ee
           from EventEntry ee
           join fetch ee.event e
           where ee.user.id = :userId
             and ee.status = :status
           """)
    List<EventEntry> findByUserIdAndStatusFetchEvent(@Param("userId") String userId,
                                                     @Param("status") RequestStatus status);

    @Query("""
      select ee from EventEntry ee
        join fetch ee.user u
        join fetch ee.event e
      where e.id = :eventId and ee.status = com.esporte.myapp.enums.RequestStatus.ACCEPTED
    """)
    List<EventEntry> findAcceptedByEventIdFetchUser(@Param("eventId") Long eventId);


}
