package com.esporte.myapp.dto;

import com.esporte.myapp.entity.FriendRequest;
import com.esporte.myapp.enums.RequestStatus;

public record FriendRequestResponse(
        Long id,
        RequestStatus status,
        UserResponse sender,
        UserResponse receiver
) {

    public static FriendRequestResponse from(FriendRequest request) {
        return new FriendRequestResponse(
                request.getId(),
                request.getStatus(),
                UserResponse.from(request.getSender()),
                UserResponse.from(request.getReceiver())
        );
    }


}