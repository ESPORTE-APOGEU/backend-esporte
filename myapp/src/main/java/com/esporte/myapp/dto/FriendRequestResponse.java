package com.esporte.myapp.dto;

import com.esporte.myapp.entity.FriendRequest;
import com.esporte.myapp.enums.RequestStatus;

public record FriendRequestResponse(
        Long id,
        RequestStatus status,
        UserResponse sender,
        UserResponse receiver,
        int mutualCount
) {

    public static FriendRequestResponse from(FriendRequest request, int mutualCount) {
        return new FriendRequestResponse(
                request.getId(),
                request.getStatus(),
                UserResponse.from(request.getSender()),
                UserResponse.from(request.getReceiver()),
                mutualCount
        );
    }
    public static FriendRequestResponse from(FriendRequest request) {
        return from(request, 0);
    }

}