package com.esporte.myapp.service;

import com.esporte.myapp.dto.FriendRequestResponse;
import com.esporte.myapp.entity.FriendRequest;
import com.esporte.myapp.entity.RequestStatus;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.FriendRequestRepository;
import com.esporte.myapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    public FriendRequestResponse createFriendRequest(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("Receiver not found"));

        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(RequestStatus.PENDING);
        request = friendRequestRepository.save(request);
        return FriendRequestResponse.from(request);
    }

    public List<FriendRequestResponse> getPendingRequests(Long receiverId) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("Receiver not found"));

        return friendRequestRepository.findByReceiverAndStatus(receiver, RequestStatus.PENDING)
                .stream()
                .map(FriendRequestResponse::from)
                .toList();
    }

    public FriendRequestResponse respondToRequest(Long requestId, RequestStatus status) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        request.setStatus(status);
        request = friendRequestRepository.save(request);
        return FriendRequestResponse.from(request);
    }
}