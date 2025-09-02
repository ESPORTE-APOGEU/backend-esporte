//// filepath: c:\Users\arthu\Documents\APOGEU_PROJECT\BTG\Esportes/backend-esporte/myapp/src/main/java/com/esporte/myapp/dto/FriendSuggestionResponse.java
package com.esporte.myapp.dto;

import java.util.List;

public record FriendSuggestionResponse(Long id, String name, String avatar, int mutualCount, List<String> mutualAvatars) {}