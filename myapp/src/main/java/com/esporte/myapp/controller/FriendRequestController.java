package com.esporte.myapp.controller;

import com.esporte.myapp.entity.FriendRequest;
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
    public ResponseEntity<FriendRequest> createRequest(@RequestParam Long senderId, @RequestParam Long receiverId) {
        FriendRequest request = friendRequestService.createFriendRequest(senderId, receiverId);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/pending/{receiverId}")
    public ResponseEntity<List<FriendRequest>> getPendingRequests(@PathVariable Long receiverId) {
        List<FriendRequest> requests = friendRequestService.getPendingRequests(receiverId);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/{requestId}/respond")
    public ResponseEntity<FriendRequest> respondToRequest(@PathVariable Long requestId, @RequestParam RequestStatus status) {
        FriendRequest request = friendRequestService.respondToRequest(requestId, status);
        return ResponseEntity.ok(request);
    }
}