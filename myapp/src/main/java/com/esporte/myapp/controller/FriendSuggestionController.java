//// filepath: c:\Users\arthu\Documents/APOGEU_PROJECT/BTG/Esportes/backend-esporte/myapp/src/main/java/com/esporte/myapp/controller/FriendSuggestionController.java
package com.esporte.myapp.controller;

import com.esporte.myapp.dto.FriendSuggestionResponse;
import com.esporte.myapp.service.FriendSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friend-suggestions")
public class FriendSuggestionController {
  private final FriendSuggestionService svc;

  @GetMapping("/{userId}")
  public List<FriendSuggestionResponse> list(@PathVariable Long userId) {
    return svc.getFriendSuggestions(userId);
  }
}