package com.esporte.myapp.controller;

import com.esporte.myapp.dto.FriendSuggestionResponse;
import com.esporte.myapp.service.FriendSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friend-suggestions")
@RequiredArgsConstructor
public class FriendSuggestionController {

    private final FriendSuggestionService friendSuggestionService;

    @GetMapping
    public ResponseEntity<List<FriendSuggestionResponse>> getSuggestions(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
         List<FriendSuggestionResponse> suggestions = friendSuggestionService.getFriendSuggestions(userId);
         return ResponseEntity.ok(suggestions);
    }
}