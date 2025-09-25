package com.esporte.myapp.controller;

import com.esporte.myapp.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/friendships")
@RequiredArgsConstructor
@Slf4j
public class FriendshipController {

    private final FriendshipService friendshipService;

    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> unfriend(@AuthenticationPrincipal Jwt jwt, @PathVariable String friendId) {
        String currentUserId = jwt.getSubject();
        log.info("=> Recebida solicitação do usuário '{}' para desfazer amizade com '{}'", currentUserId, friendId);
        friendshipService.removeFriendship(currentUserId, friendId);
        log.info("<= Amizade desfeita com sucesso.");
        return ResponseEntity.noContent().build();
    }
}