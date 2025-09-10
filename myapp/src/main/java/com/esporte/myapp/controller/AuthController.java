package com.esporte.myapp.controller;

import com.esporte.myapp.dto.AuthRequest;
import com.esporte.myapp.dto.AuthResponse;
import com.esporte.myapp.dto.UserRequest;
import com.esporte.myapp.dto.UserResponse;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.UserRepository;
import com.esporte.myapp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;


    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
    // AuthController.java (trecho)
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(
            @AuthenticationPrincipal Jwt jwt,
            org.springframework.security.core.Authentication authentication
    ) {
        // Tenta obter o sub do Jwt (resource server)
        String clerkId = (jwt != null) ? jwt.getSubject() : null;

        // Se vier de filtro custom, pega do Authentication
        if (clerkId == null && authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof String s) {
                clerkId = s;               // ex.: "user_abc"
            } else {
                clerkId = authentication.getName(); // fallback
            }
        }

        if (clerkId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autenticado");
        }

        User user = userRepository.findById(clerkId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        UserResponse resp = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getBirthday(),
                user.getGender(),
                user.getCity(),
                user.getSports()
        );
        return ResponseEntity.ok(resp);
    }

}
