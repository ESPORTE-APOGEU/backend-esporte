//// filepath: c:\Users\arthu\Documents/APOGEU_PROJECT/BTG/Esportes/backend-esporte/myapp/src/main/java/com/esporte/myapp/controller/FriendSuggestionController.java
package com.esporte.myapp.controller;

import com.esporte.myapp.dto.FriendSuggestionResponse;
import com.esporte.myapp.service.FriendSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friend-suggestions")
@RequiredArgsConstructor
public class FriendSuggestionController {

    private final FriendSuggestionService friendSuggestionService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<FriendSuggestionResponse>> getSuggestions(@PathVariable Long userId) {
         List<FriendSuggestionResponse> suggestions = friendSuggestionService.getFriendSuggestions(userId);
         return ResponseEntity.ok(suggestions);
    }
}