package com.esporte.myapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import com.esporte.myapp.entity.Event;

public class EventResponse {
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
    private Long organizerId;
    private String organizerPhoto;
    // Se desejar, adicione também:
    private Long organizerName;
    
    // Construtor
    public EventResponse(Long id, String name, String location, String sport, String level, String gender, LocalDate date, LocalTime startTime, LocalTime endTime, BigDecimal price, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.sport = sport;
        this.level = level;
        this.gender = gender;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.description = description;
    }

    public EventResponse(Long id2, String name2, String description2) {
    }

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

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public String getOrganizerPhoto() {
        return organizerPhoto;
    }

    public void setOrganizerPhoto(String organizerPhoto) {
        this.organizerPhoto = organizerPhoto;
    }

    public Long getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(Long organizerName) {
        this.organizerName = organizerName;
    }

    public static EventResponse from(Event event) {
        EventResponse resp = new EventResponse(event.getId(), event.getName(), event.getDescription());
        resp.setId(event.getId());
        resp.setName(event.getName());
        resp.setLocation(event.getLocation());
        resp.setSport(event.getSport());
        resp.setLevel(event.getLevel());
        resp.setGender(event.getGender());
        resp.setDate(event.getDate());
        resp.setStartTime(event.getStartTime());
        resp.setEndTime(event.getEndTime());
        resp.setPrice(event.getPrice() != null ? BigDecimal.valueOf(event.getPrice()) : null);
        resp.setDescription(event.getDescription());
        resp.setOrganizerId(event.getOrganizerId());
        resp.setOrganizerPhoto(event.getOrganizerPhoto());
        // Se a entidade possuir o nome do organizador
        resp.setOrganizerName(event.getOrganizerId());
        return resp;
    }
}
