package com.esporte.myapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    private String id; // ID do Clerk (não é mais auto-incrementado)

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

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

    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String city;

    @ElementCollection
    @CollectionTable(name = "user_sports", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "sport")
    private List<String> sports = new ArrayList<>();
}
