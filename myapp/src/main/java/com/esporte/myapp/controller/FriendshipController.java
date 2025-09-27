package com.esporte.myapp.controller;

import com.esporte.myapp.dto.MutualFriendsDTO;
import com.esporte.myapp.dto.UserResponse;
import com.esporte.myapp.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<UserResponse>> getUserFriends(@PathVariable String id) {
        return ResponseEntity.ok(friendshipService.getFriendsOf(id));
    }

    // controller/FriendshipController.java
    @GetMapping("/mutual/{otherId}")
    public ResponseEntity<MutualFriendsDTO> getMutualFriends(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String otherId
    ) {
        String meId = jwt.getSubject();
        var mutual = friendshipService.findMutualFriends(meId, otherId);

        // mande todos e deixe o app decidir quantos mostrar, ou corte aqui:
        int total = mutual.size();
        var top = mutual.stream()
                .limit(3) // os 3 avatares do Figma
                .map(UserResponse::from)
                .toList();

        return ResponseEntity.ok(new MutualFriendsDTO(total, top));
    }

}