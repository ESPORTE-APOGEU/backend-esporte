package com.esporte.myapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "event_entry")
public class EventEntry extends BaseUserRelatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "event_id")
    private Long eventId;
    
    // O campo user_id é gerenciado pela herança em BaseUserRelatedEntity
    
    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Column(name = "status") // ajuste para o nome real da coluna se for diferente
    private String status; // Ex.: "PENDING", "ACCEPTED", "DECLINED"
}