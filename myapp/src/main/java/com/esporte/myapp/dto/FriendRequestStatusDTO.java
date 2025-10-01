package com.esporte.myapp.dto;

public record FriendRequestStatusDTO(
        boolean isFriend,
        boolean pendingOutgoing,  // me -> other
        boolean pendingIncoming,  // other -> me
        Long requestId            // se houver pendente
) {}