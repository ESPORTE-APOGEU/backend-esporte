package com.esporte.myapp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.esporte.myapp.enums.Gender;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String id; // ID do Clerk

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String city;

    @ElementCollection
    @CollectionTable(name = "user_sports", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "sport")
    private List<String> sports = new ArrayList<>();

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

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getSports() {
        return sports;
    }

    public void setSports(List<String> sports) {
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
