package com.esporte.myapp.dto;

import lombok.Data;

@Data
public class EventEntryRequest {
    private Long eventId;
    private Long userId;
}