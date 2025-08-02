package com.esporte.myapp.service;

import com.esporte.myapp.dto.AuthRequest;
import com.esporte.myapp.dto.AuthResponse;
import com.esporte.myapp.dto.UserRequest;
import com.esporte.myapp.dto.UserResponse;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.UserRepository;
import com.esporte.myapp.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(UserRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciais inválidas");
        }

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token);
    }
}
