package com.esporte.myapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

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