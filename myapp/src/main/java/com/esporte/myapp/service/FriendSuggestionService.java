//// filepath: c:\Users\arthu\Documents\APOGEU_PROJECT\BTG/Esportes/backend-esporte/myapp/src/main/java/com/esporte/myapp/service/FriendSuggestionService.java
package com.esporte.myapp.service;

import com.esporte.myapp.dto.FriendSuggestionResponse;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.FriendRequestRepository;
import com.esporte.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendSuggestionService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    public List<FriendSuggestionResponse> getFriendSuggestions(Long userId) {
        User currentUser = userRepository.findById(userId)
          .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Lista de amigos aceitos do usuário logado
        List<User> myFriends = friendRequestRepository.findFriendsOfUser(currentUser);
        
        // Obter todos os usuários que não sejam o usuário logado nem seus amigos
        List<User> allUsers = userRepository.findAll();
        List<User> candidates = allUsers.stream()
          .filter(u -> !u.getId().equals(userId) && !myFriends.contains(u))
          .collect(Collectors.toList());
        
        List<FriendSuggestionResponse> suggestions = new ArrayList<>();
        for (User candidate : candidates) {
            List<User> candidateFriends = friendRequestRepository.findFriendsOfUser(candidate);
            // Calcular amigos em comum (interseção)
            Set<User> mutual = new HashSet<>(myFriends);
            mutual.retainAll(candidateFriends);
            int count = mutual.size();
            if(count > 0) {
                // Supondo que User possua método getPhoto() para obter o avatar
                List<String> mutualAvatars = mutual.stream()
                    .map(User::getPhoto)
                    .limit(3)
                    .collect(Collectors.toList());
                suggestions.add(new FriendSuggestionResponse(candidate.getId(), candidate.getName(), candidate.getPhoto(), count, mutualAvatars));
            }
        }
        // Ordena as sugestões pela quantidade de amigos em comum (decrescente)
        suggestions.sort((a, b) -> b.mutualCount() - a.mutualCount());
        return suggestions;
    }
}