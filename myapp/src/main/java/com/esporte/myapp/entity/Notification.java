package com.esporte.myapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "notification")
public class Notification extends BaseUserRelatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Removemos o campo userId; use o relacionamento herdado

    private String type;
    private String iconName;
    private String title;
    private String description;
    private LocalDateTime timestamp;
    private String tagText;
    private String tagIcon;

    @Column(name = "related_event_id")
    private Long relatedEventId;
}