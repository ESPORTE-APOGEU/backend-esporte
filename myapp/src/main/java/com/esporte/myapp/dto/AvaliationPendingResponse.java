package com.esporte.myapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliationPendingResponse {
    private Long avaliationId;
    private String toUserName;
    private String toUserPhoto;
}