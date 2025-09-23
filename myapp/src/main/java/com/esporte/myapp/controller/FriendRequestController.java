package com.esporte.myapp.controller;

import com.esporte.myapp.entity.FriendRequest;
import com.esporte.myapp.enums.RequestStatus;
import com.esporte.myapp.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friend-requests")
@RequiredArgsConstructor
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @PostMapping
    public ResponseEntity<FriendRequest> createRequest(@AuthenticationPrincipal Jwt jwt, @RequestParam String receiverId) {
        String senderId = jwt.getSubject();
        FriendRequest request = friendRequestService.createFriendRequest(senderId, receiverId);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FriendRequest>> getPendingRequests(@AuthenticationPrincipal Jwt jwt) {
        String receiverId = jwt.getSubject();
        List<FriendRequest> requests = friendRequestService.getPendingRequests(receiverId);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/{requestId}/respond")
    public ResponseEntity<FriendRequest> respondToRequest(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String requestId,
            @RequestParam RequestStatus status) {
        String userId = jwt.getSubject();
        FriendRequest request = friendRequestService.respondToRequest(userId, requestId, status);
        return ResponseEntity.ok(request);
    }
}