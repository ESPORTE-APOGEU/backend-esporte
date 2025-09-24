//// filepath: c:\Users\arthu\Documents\APOGEU_PROJECT\BTG/Esportes/backend-esporte/myapp/src/main/java/com/esporte/myapp/service/FriendSuggestionService.java
package com.esporte.myapp.service;

import com.esporte.myapp.dto.FriendSuggestionResponse;
import com.esporte.myapp.entity.FriendRequest;
import com.esporte.myapp.entity.RequestStatus;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.FriendRequestRepository;
import com.esporte.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FriendSuggestionService {
    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<FriendSuggestionResponse> getFriendSuggestions(Long userId) {
        User currentUser = userRepository.findById(userId)
          .orElseThrow(() -> new RuntimeException("User not found"));

        // Força inicialização antes de sair do escopo
        Set<String> currentSports = new HashSet<>(optionalSports(currentUser));

        Set<User> myFriends = new HashSet<>();
        List<FriendRequest> asReceiver = friendRequestRepository.findByReceiverAndStatus(currentUser, RequestStatus.ACCEPTED);
        asReceiver.forEach(fr -> myFriends.add(fr.getSender()));
        List<FriendRequest> asSender = friendRequestRepository.findBySenderAndStatus(currentUser, RequestStatus.ACCEPTED);
        asSender.forEach(fr -> myFriends.add(fr.getReceiver()));

        List<User> candidates = userRepository.findAll().stream()
                .filter(u -> !u.getId().equals(userId) && !myFriends.contains(u))
                .toList();

        List<FriendSuggestionResponse> suggestions = new ArrayList<>();

        // 1) >= 3 amigos em comum
        for (User candidate : candidates) {
            Set<User> candidateFriends = buildFriendSet(candidate);
            Set<User> mutual = new HashSet<>(myFriends);
            mutual.retainAll(candidateFriends);
            if (mutual.size() >= 3) {
                List<String> mutualAvatars = mutual.stream()
                        .map(User::getPhoto)
                        .filter(Objects::nonNull)
                        .limit(3)
                        .toList();
                suggestions.add(new FriendSuggestionResponse(
                        candidate.getId(), candidate.getName(), candidate.getPhoto(),
                        mutual.size(), mutualAvatars));
            }
        }

        // 2) Sem amigos em comum + >=2 esportes iguais
        for (User candidate : candidates) {
            if (suggestions.stream().anyMatch(s -> s.id().equals(candidate.getId()))) continue;

            Set<User> candidateFriends = buildFriendSet(candidate);
            Set<User> mutual = new HashSet<>(myFriends);
            mutual.retainAll(candidateFriends);
            if (mutual.isEmpty()) {
                Set<String> candSports = new HashSet<>(optionalSports(candidate));
                Set<String> inter = new HashSet<>(currentSports);
                inter.retainAll(candSports);
                if (inter.size() >= 2) {
                    suggestions.add(new FriendSuggestionResponse(
                            candidate.getId(), candidate.getName(), candidate.getPhoto(),
                            0, List.of()));
                }
            }
        }

        suggestions.sort((a, b) -> b.mutualCount() - a.mutualCount());
        return suggestions;
    }

    private Set<String> optionalSports(User u) {
        try {
            return u.getSports() == null ? Set.of() : u.getSports();
        } catch (Exception e) {
            return Set.of();
        }
    }

    private Set<User> buildFriendSet(User user) {
        Set<User> friends = new HashSet<>();
        friendRequestRepository.findByReceiverAndStatus(user, RequestStatus.ACCEPTED)
                .forEach(fr -> friends.add(fr.getSender()));
        friendRequestRepository.findBySenderAndStatus(user, RequestStatus.ACCEPTED)
                .forEach(fr -> friends.add(fr.getReceiver()));
        return friends;
    }
}