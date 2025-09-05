package com.esporte.myapp.entity;

import jakarta.persistence.*;
import lombok.*;
<<<<<<< HEAD
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

<<<<<<< HEAD
    public Long getImageUrl() {
        throw new UnsupportedOperationException("Unimplemented method 'getImageUrl'");
    }
=======
    // Novo campo para armazenar o avatar/foto do usuário
    private String photo;
>>>>>>> parent of 54d1e99 (Merge branch 'dev' into origin/feat/back-amizade)
}
