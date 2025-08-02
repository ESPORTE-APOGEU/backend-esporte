package com.esporte.myapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "event_participants")
public class EventParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "event_id")
    private Long eventId;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "participant_name")
    private String participantName;
    
    @Column(name = "participant_photo")
    private String participantPhoto;
}
