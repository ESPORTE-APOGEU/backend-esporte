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


    @ElementCollection(fetch = FetchType.EAGER) // <-- ALTERAR AQUI
    @CollectionTable(name = "user_sports", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "sport")
    private List<String> sports = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    public User() {}

    public User(String id, String name, String email, LocalDateTime createdAt,
                LocalDate birthday, Gender gender, String city, List<String> sports) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.birthday = birthday;
        this.gender = gender;
        this.city = city;
        this.sports = sports;
    }

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
