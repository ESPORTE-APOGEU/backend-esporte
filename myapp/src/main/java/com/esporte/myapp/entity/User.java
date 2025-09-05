package com.esporte.myapp.entity;

import jakarta.persistence.*;
import lombok.*;
<<<<<<< HEAD
<<<<<<< HEAD
=======

import java.time.LocalDate;
>>>>>>> parent of 22174b3 (.)
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

=======

import java.time.LocalDateTime;
>>>>>>> parent of 54d1e99 (Merge branch 'dev' into origin/feat/back-amizade)

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

<<<<<<< HEAD
<<<<<<< HEAD
    public Long getImageUrl() {
        throw new UnsupportedOperationException("Unimplemented method 'getImageUrl'");
    }
=======
    // Novo campo para armazenar o avatar/foto do usuário
    private String photo;
>>>>>>> parent of 54d1e99 (Merge branch 'dev' into origin/feat/back-amizade)
=======
    private String photo;

    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String city;

    @ElementCollection
    @CollectionTable(name = "user_sports", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "sport")
    private List<String> sports = new ArrayList<>();

>>>>>>> parent of 22174b3 (.)
}
