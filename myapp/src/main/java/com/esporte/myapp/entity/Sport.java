package com.esporte.myapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sports")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=80)
    private String name;
}
