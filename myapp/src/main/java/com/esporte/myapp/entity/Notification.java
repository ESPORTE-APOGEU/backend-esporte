package com.esporte.myapp.entity;

import com.esporte.myapp.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "notification",
        indexes = { @Index(name = "idx_notification_user_status", columnList = "user_id,status") })

public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // IMPORTANTE: seu User tem ID String (Clerk)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String type;         // ex.: entry_request, entry_accepted, entry_declined

    @Column(length = 50)
    private String iconName;     // ex.: "info" (usado no front)

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(length = 40)
    private String tagText;

    @Column(length = 40)
    private String tagIcon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status = NotificationStatus.NEW;

    private LocalDateTime readAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime archivedAt;

    // Relacionamentos (substituem os IDs soltos):
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_event_id")
    private Event relatedEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_id")
    private EventEntry relatedEntry;

    @PrePersist
    public void prePersist() {
        if (timestamp == null) timestamp = LocalDateTime.now();
    }
}
