package com.esporte.myapp.service;

import com.esporte.myapp.entity.User;
import com.esporte.myapp.dto.UserRequest;
import com.esporte.myapp.dto.UserResponse;
import com.esporte.myapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserResponse create(UserRequest req) {
        if (repo.existsById(Long.valueOf(req.id()))) {
            throw new IllegalArgumentException("ID já cadastrado");
        }

        User user = new User();
        user.setId(req.id());
        user.setName(req.name());
        user.setEmail(req.email());
        user.setBirthday(req.birthday());
        user.setGender(req.gender());
        user.setCity(req.city());
        user.setSports(req.sports());

        user = repo.save(user);
        return toResponse(user);
    }

    public UserResponse get(Long id) {
        User u = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return toResponse(u);
    }

    private UserResponse toResponse(User u) {
        return new UserResponse(
                u.getId(), u.getName(), u.getEmail(), u.getCreatedAt(),
                u.getBirthday(), u.getGender(), u.getCity(), u.getSports()
        );
    }
}
