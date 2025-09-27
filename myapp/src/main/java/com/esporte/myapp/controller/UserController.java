package com.esporte.myapp.controller;

import com.esporte.myapp.dto.UserRequest;
import com.esporte.myapp.dto.UserResponse;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    /**
     * Retorna o perfil do usuário autenticado (pelo sub do JWT).
     * 200 se existe, 404 se não existe.
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(
            @AuthenticationPrincipal Jwt jwt,
            Authentication authentication
    ) {
        String subject = (jwt != null) ? jwt.getSubject()
                : (authentication != null ? authentication.getName() : null);

        if (subject == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return service.findById(subject)
                .map(u -> ResponseEntity.ok(UserResponse.from(u)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/me")
// UserController.upsertMe(...)
    public ResponseEntity<UserResponse> upsertMe(
            @AuthenticationPrincipal Jwt jwt,
            Authentication authentication,
            @Valid @RequestBody UserRequest req
    ) {
        String subject = (jwt != null) ? jwt.getSubject()
                : (authentication != null ? authentication.getName() : null);
        if (subject == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        // tenta preencher photo com claims do Clerk se o app não mandou
        String photoFromJwt = firstNonBlank(
                jwt != null ? jwt.getClaimAsString("picture") : null,
                jwt != null ? jwt.getClaimAsString("image_url") : null,
                jwt != null ? jwt.getClaimAsString("avatar") : null
        );

        UserRequest fixed = new UserRequest(
                req.id(), req.name(), req.email(), req.birthday(), req.gender(),
                req.city(), req.sports(),
                (req.photo() != null && !req.photo().isBlank()) ? req.photo() : photoFromJwt
        );

        User saved = service.upsert(subject, fixed);
        return ResponseEntity.ok(UserResponse.from(saved));
    }

    private static String firstNonBlank(String... vals) {
        for (String v : vals) if (v != null && !v.isBlank()) return v;
        return null;
    }

    // --- Abaixo, rotas administrativas/diagnóstico (opcionais) ---

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable String id) {
        return service.findById(id)
                .map(u -> ResponseEntity.ok(UserResponse.from(u)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(service.getAll().stream().map(UserResponse::from).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/sports")
    public ResponseEntity<UserResponse> updateMySports(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody List<String> sports
    ) {
        return ResponseEntity.ok(service.updateSports(jwt.getSubject(), sports));
    }



}
