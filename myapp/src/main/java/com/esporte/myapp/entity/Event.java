package com.esporte.myapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
<<<<<<< HEAD

<<<<<<< HEAD
import org.locationtech.jts.geom.Point;

=======
>>>>>>> parent of 22174b3 (.)
=======
>>>>>>> parent of 3e4dcc1 (.)
@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private String sport;
    private String level;
    private String gender;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal price;
    private String description;
<<<<<<< HEAD
<<<<<<< HEAD
    private Long organizerId;
    private String organizerPhoto;
=======
>>>>>>> parent of 54d1e99 (Merge branch 'dev' into origin/feat/back-amizade)
=======
    @Column(name = "location_point", columnDefinition = "geography(Point,4326)")
    private Point locationPoint;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "creator_id", nullable = false)
//    private User creator;

>>>>>>> parent of 22174b3 (.)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
<<<<<<< HEAD

    public Point getLocationPoint() { return locationPoint; }
    public void setLocationPoint(Point locationPoint) { this.locationPoint = locationPoint; }

//    public User getCreator() {return creator;}
//    public void setCreator(User creator) {this.creator = creator;}

<<<<<<< HEAD
    public String getOrganizerPhoto() {
        return organizerPhoto;
    }

    public void setOrganizerPhoto(String organizerPhoto) {
        this.organizerPhoto = organizerPhoto;
    }
<<<<<<< HEAD

    public void setLocationPoint(Point point) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setLocationPoint'");
    }
=======
>>>>>>> parent of 54d1e99 (Merge branch 'dev' into origin/feat/back-amizade)
=======
>>>>>>> parent of 22174b3 (.)
=======
>>>>>>> parent of 3e4dcc1 (.)
}
