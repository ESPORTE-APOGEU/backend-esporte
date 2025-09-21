package com.esporte.myapp.dto;

import com.esporte.myapp.entity.User;
import com.esporte.myapp.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public record UserResponse(
        String id,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDate birthday,
        Gender gender,
        String city,
        List<String> sports,
        String photo
) {
    public static UserResponse from(User u) {
        return new UserResponse(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getCreatedAt(),
                u.getBirthday(),
                u.getGender(),
                u.getCity(),
                u.getSports(),
                u.getPhoto()
        );
    }
}
