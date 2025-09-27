// src/main/java/com/esporte/myapp/dto/EventParticipantsResponse.java
package com.esporte.myapp.dto;

import java.util.List;

public record EventParticipantsResponse(
        List<ParticipantDTO> participants,
        Integer maxParticipants,
        long acceptedCount,
        boolean iAmParticipant
) {}
