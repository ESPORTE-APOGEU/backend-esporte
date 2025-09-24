package com.esporte.myapp.controller;

import com.esporte.myapp.dto.FriendRequestResponse;
import com.esporte.myapp.entity.RequestStatus;
import com.esporte.myapp.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friend-requests")
@RequiredArgsConstructor
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @PostMapping
    public ResponseEntity<FriendRequestResponse> createRequest(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return ResponseEntity.ok(friendRequestService.createFriendRequest(senderId, receiverId));
    }

    @GetMapping("/pending/{receiverId}")
    public ResponseEntity<List<FriendRequestResponse>> getPendingRequests(@PathVariable Long receiverId) {
        return ResponseEntity.ok(friendRequestService.getPendingRequests(receiverId));
    }

    @PostMapping("/{requestId}/respond")
    public ResponseEntity<FriendRequestResponse> respondToRequest(@PathVariable Long requestId, @RequestParam RequestStatus status) {
        return ResponseEntity.ok(friendRequestService.respondToRequest(requestId, status));
    }
}