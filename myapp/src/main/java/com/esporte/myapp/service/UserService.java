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
        repo.findByEmail(req.email()).ifPresent(u -> {
            throw new IllegalArgumentException("Email já cadastrado");
        });

        User user = new User();
        user.setName(req.name());
        user.setEmail(req.email());
        user.setPasswordHash(passwordEncoder.encode(req.password()));

        user = repo.save(user);
        return toResponse(user);
    }

    public UserResponse get(Long id) {
        User u = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return toResponse(u);
    }

    private UserResponse toResponse(User u) {
        return new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getCreatedAt());
    }
}
