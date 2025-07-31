package com.esporte.myapp.repository;

import com.esporte.myapp.entity.EventEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventEntryRepository extends JpaRepository<EventEntry, Long> {
}