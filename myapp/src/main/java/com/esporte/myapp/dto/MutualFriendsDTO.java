// dto/MutualFriendsDTO.java
package com.esporte.myapp.dto;

import java.util.List;

public record MutualFriendsDTO(
        int total,
        List<UserResponse> users // os primeiros N (ex: 3) para mostrar avatar/nome
) {}
