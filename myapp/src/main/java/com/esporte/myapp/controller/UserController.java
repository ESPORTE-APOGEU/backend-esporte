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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    // Cria o próprio usuário (id SEMPRE = sub do token)
    @PostMapping("/me")
    public ResponseEntity<UserResponse> createMe(
            @Valid @RequestBody UserRequest req,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String sub = jwt.getSubject();
        var fixed = new UserRequest(
                sub,
                req.name(),
                req.email(),
                req.birthday(),
                req.gender(),
                req.city(),
                req.sports(),
                req.photo()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(fixed));
    }

    // Retorna o próprio perfil
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.get(jwt.getSubject()));
    }

    // Atualiza campos do próprio perfil
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UserRequest req
    ) {
        return ResponseEntity.ok(service.update(jwt.getSubject(), req));
    }

    // Atualiza SOMENTE a lista de esportes do próprio perfil
    @PatchMapping("/me/sports")
    public ResponseEntity<UserResponse> updateMySports(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody List<String> sports
    ) {
        return ResponseEntity.ok(service.updateSports(jwt.getSubject(), sports));
    }

    // --- Rotas administrativas/diagnóstico (opcionais) ---

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Remove o próprio perfil
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal Jwt jwt) {
        service.delete(jwt.getSubject());
        return ResponseEntity.noContent().build();
    }
}
