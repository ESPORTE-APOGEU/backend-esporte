package com.esporte.myapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "event_entry")
public class EventEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "event_id")
    private Long eventId;
    
    @Column(name = "user_id") // Define explicitamente evitando conflito
    private Long userId;
    
    @Column(name = "requested_at")
    private LocalDateTime requestedAt;
}