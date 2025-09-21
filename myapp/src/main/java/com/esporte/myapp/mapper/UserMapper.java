// src/main/java/com/esporte/myapp/mapper/UserMapper.java
package com.esporte.myapp.mapper;

import com.esporte.myapp.dto.SportDTO;
import com.esporte.myapp.dto.UserResponse;
import com.esporte.myapp.entity.User;

import java.util.List;

public class UserMapper {
    public static UserResponse toResponse(User u) {
        List<SportDTO> sports = (u.getSports() == null) ? List.of()
                : u.getSports().stream()
                .map(s -> new SportDTO(s.getId(), s.getName()))
                .toList();

        return new UserResponse(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getCreatedAt(),
                u.getBirthday(),
                u.getGender(),
                u.getCity(),
                sports,
                u.getPhoto() // <-- agora UserResponse pede photo também
        );
    }
}
