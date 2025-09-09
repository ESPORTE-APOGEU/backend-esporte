// src/main/java/com/esporte/myapp/service/AuthService.java
package com.esporte.myapp.service;

import com.esporte.myapp.dto.AuthRequest;
import com.esporte.myapp.dto.AuthResponse;
import com.esporte.myapp.dto.UserRequest;
import com.esporte.myapp.dto.UserResponse;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.UserRepository;
import com.esporte.myapp.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserResponse register(UserRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        User user = new User();
        user.setId(request.id());              // <-- sua entidade usa id String
        user.setName(request.name());
        user.setEmail(request.email());
        user.setBirthday(request.birthday());  // <-- campo é birthday
        user.setGender(request.gender());
        user.setCity(request.city());
        user.setSports(request.sports());

        userRepository.save(user);

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getBirthday(),
                user.getGender(),
                user.getCity(),
                user.getSports()
        );
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Sem senha: confie no provedor externo (Clerk/OAuth) e gere o token
        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token);
    }
}
