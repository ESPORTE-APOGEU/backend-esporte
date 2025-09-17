package com.esporte.myapp.repository;

import com.esporte.myapp.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

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

    List<Event> findByDateGreaterThanEqualAndCreator_NameContainingIgnoreCase(LocalDate date, String creatorName);

    List<Event> findByDateGreaterThanEqualAndSportContainingIgnoreCase(LocalDate date, String sport);

    List<Event> findByDateGreaterThanEqualAndDescriptionContainingIgnoreCase(LocalDate date, String description);


}