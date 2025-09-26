package com.esporte.myapp.repository;

import com.esporte.myapp.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    @Query(value = """
        SELECT * FROM events
        WHERE ST_DWithin(
            location_point,
            ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography,
            :radius
        )
        """, nativeQuery = true)
    List<Event> findWithinRadius(
        @Param("lat") double lat,
        @Param("lon") double lon,
        @Param("radius") double radiusMeters
    );

    List<Event> findByDateGreaterThanEqualAndNameContainingIgnoreCase(LocalDate date, String name);

//    List<Event> findByDateGreaterThanEqualAndCreator_NameContainingIgnoreCase(LocalDate date, String creatorName);

    List<Event> findByDateGreaterThanEqualAndSportContainingIgnoreCase(LocalDate date, String sport);

    List<Event> findByDateGreaterThanEqualAndDescriptionContainingIgnoreCase(LocalDate date, String description);

    @Query("""
       select e from Event e
       join fetch e.creator c
       where e.id = :id
    """)
    Optional<Event> findByIdWithCreator(@Param("id") Long id);

    List<Event> findByCreator_Id(String creatorId);

    @Query("""
        select e
          from Event e
         where e.startReminderSentAt is null
           and e.date between :fromDate and :toDate
    """)
    List<Event> findCandidatesForStartReminder(LocalDate fromDate, LocalDate toDate);

}