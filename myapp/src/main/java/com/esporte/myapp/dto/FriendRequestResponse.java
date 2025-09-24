package com.esporte.myapp.dto;

import com.esporte.myapp.entity.FriendRequest;
import com.esporte.myapp.entity.RequestStatus;

import java.time.LocalDateTime;

public record FriendRequestResponse(
        Long id,
        Long senderId,
        String senderName,
        String senderPhoto,
        Long receiverId,
        String receiverName,
        RequestStatus status,
        LocalDateTime createdAt
) {
    public static FriendRequestResponse from(FriendRequest fr) {
        return new FriendRequestResponse(
                fr.getId(),
                fr.getSender().getId(),
                fr.getSender().getName(),
                fr.getSender().getPhoto(),
                fr.getReceiver().getId(),
                fr.getReceiver().getName(),
                fr.getStatus(),
                fr.getCreatedAt()
        );
    }
}