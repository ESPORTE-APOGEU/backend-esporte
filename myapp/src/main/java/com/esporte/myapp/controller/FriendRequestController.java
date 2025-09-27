package com.esporte.myapp.controller;

import com.esporte.myapp.dto.FriendRequestResponse;
import com.esporte.myapp.entity.FriendRequest;
import com.esporte.myapp.enums.RequestStatus;
import com.esporte.myapp.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Importa a anotação
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friend-requests")
@RequiredArgsConstructor
@Slf4j // Adiciona o logger
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @PostMapping
    public ResponseEntity<FriendRequestResponse> createRequest(@AuthenticationPrincipal Jwt jwt, @RequestParam String receiverId) {
        String senderId = jwt.getSubject();
        log.info("=> Recebida solicitação de amizade do usuário '{}' para o usuário '{}'", senderId, receiverId);
        FriendRequestResponse request = friendRequestService.createFriendRequest(senderId, receiverId);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FriendRequestResponse>> getPendingRequests(@AuthenticationPrincipal Jwt jwt) {
        String receiverId = jwt.getSubject();
        log.info("=> Buscando solicitações de amizade pendentes para o usuário '{}'", receiverId);
        List<FriendRequestResponse> requests = friendRequestService.getPendingRequests(receiverId);
        log.info("<= Encontradas {} solicitações pendentes para o usuário '{}'", requests.size(), receiverId);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/{requestId}/respond")
    public ResponseEntity<FriendRequestResponse> respondToRequest(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String requestId,
            @RequestParam RequestStatus status) {
        String userId = jwt.getSubject();
        log.info("=> Usuário '{}' respondendo à solicitação '{}' com o status '{}'", userId, requestId, status);
        FriendRequestResponse request = friendRequestService.respondToRequest(userId, requestId, status);
        log.info("<= Solicitação '{}' atualizada com sucesso para o status '{}'", requestId, status);
        return ResponseEntity.ok(request);
    }
}