package com.esporte.myapp.entity;


import jakarta.persistence.*;
import lombok.*;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.esporte.myapp.enums.Gender;

@Entity
@Table(name = "users")
@Getter @Setter @AllArgsConstructor
public class User {

    @Id
    private String id; // ID do Clerk

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


    private String photo;

    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String city;

    @ManyToMany(fetch = FetchType.LAZY) // EAGER só se precisar mesmo
    @JoinTable(
            name = "user_sports",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "sport_id")
    )
    private List<Sport> sports = new ArrayList<>();

    public User() {}

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", birthday=" + birthday +
                ", gender=" + gender +
                ", city='" + city + '\'' +
                ", sports=" + sports +
                '}';
    }


}
