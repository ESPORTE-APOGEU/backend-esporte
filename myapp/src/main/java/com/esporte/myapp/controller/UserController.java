package com.esporte.myapp.controller;

import com.esporte.myapp.dto.UserRequest;
import com.esporte.myapp.dto.UserResponse;
import com.esporte.myapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    // Cria/atualiza o próprio usuário (usa sub do token, ignora id do body)
    @PostMapping("/me")
    public ResponseEntity<UserResponse> createMe(@Valid @RequestBody UserRequest req,
                                                 @AuthenticationPrincipal Jwt jwt) {
        String sub = jwt.getSubject();
        var fixed = new UserRequest(
                sub,
                req.name(),
                req.email(),
                req.birthday(),
                req.gender(),
                req.city(),
                req.sports(),  // nomes dos esportes
                req.photo()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(fixed));
    }

    @GetMapping("/me")
    public UserResponse getMe(@AuthenticationPrincipal Jwt jwt) {
        return service.get(jwt.getSubject());
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(@AuthenticationPrincipal Jwt jwt,
                                                 @RequestBody UserRequest req) {
        return ResponseEntity.ok(service.update(jwt.getSubject(), req));
    }

    @PatchMapping("/me/sports")
    public ResponseEntity<UserResponse> updateMySports(@AuthenticationPrincipal Jwt jwt,
                                                       @RequestBody List<String> sports) {
        return ResponseEntity.ok(service.updateSports(jwt.getSubject(), sports));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal Jwt jwt) {
        service.delete(jwt.getSubject());
        return ResponseEntity.noContent().build();
    }
}
