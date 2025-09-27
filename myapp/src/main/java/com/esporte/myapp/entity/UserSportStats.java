// com.esporte.myapp.entity.UserSportStats
package com.esporte.myapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_sport_stats",
        uniqueConstraints = @UniqueConstraint(name = "uq_user_sport", columnNames = {"user_id", "sport"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserSportStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // se preferir, mapeie como String userId simples
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 80)
    private String sport;

    @Column(name = "total_skill", nullable = false)
    private Integer totalSkill = 0;

    @Column(name = "total_received_evaluations", nullable = false)
    private Integer totalReceivedEvaluations = 0;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
