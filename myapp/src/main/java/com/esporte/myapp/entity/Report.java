package com.esporte.myapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Type {
        CONDUTA_ANTIDESPORTIVA,
        FRAUDE_PAGAMENTO,
        INFRAESTRUTURA_PRECARIA,
        OUTROS
    }
}
