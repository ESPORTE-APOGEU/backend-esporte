package com.esporte.myapp.entity;

import com.esporte.myapp.enums.RequestStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "event_entry")
public class EventEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;


    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Enumerated(EnumType.STRING) // <- ESSENCIAL
    @Column(name = "status") // ajuste para o nome real da coluna se for diferente
    private RequestStatus status; // Ex.: "PENDING", "ACCEPTED", "DECLINED"

    @PrePersist
    public void prePersist() {
        if (requestedAt == null) requestedAt = LocalDateTime.now();
        if (status == null) status = RequestStatus.PENDING;
    }
}