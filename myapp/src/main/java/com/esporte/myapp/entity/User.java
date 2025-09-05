package com.esporte.myapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "toUser")
    private List<Avaliation> receivedAvaliations = new ArrayList<>();

    @Column(name = "total_skill")
    private Integer totalSkill;

    @Column(name = "total_rating")
    private Integer totalRating = 0;

    @Column(name = "total_received_evaluations")
    private Integer totalReceivedEvaluations = 0;

}