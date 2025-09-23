package com.esporte.myapp.service;

import com.esporte.myapp.dto.FriendSuggestionResponse;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.FriendRequestRepository;
import com.esporte.myapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendSuggestionService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Transactional(readOnly = true)
    public List<FriendSuggestionResponse> getFriendSuggestions(String userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        if (currentUser.getCity() == null || currentUser.getCity().isEmpty() || currentUser.getSports().isEmpty()) {
            return List.of();
        }

        Set<String> excludedIds = new HashSet<>();
        excludedIds.add(currentUser.getId());

        friendRequestRepository.findFriendsOfUser(currentUser)
                .forEach(friend -> excludedIds.add(friend.getId()));

        friendRequestRepository.findPendingRequestsInvolvingUser(currentUser)
                .forEach(request -> {
                    excludedIds.add(request.getSender().getId());
                    excludedIds.add(request.getReceiver().getId());
                });

        List<User> suggestedUsers = userRepository.findSuggestions(
                currentUser.getId(),
                currentUser.getCity(),
                currentUser.getSports(),
                excludedIds
        );

        Set<String> currentUserSportsSet = new HashSet<>(currentUser.getSports());

        return suggestedUsers.stream()
                .map(suggestion -> {
                    List<String> commonSports = suggestion.getSports().stream()
                            .filter(currentUserSportsSet::contains)
                            .collect(Collectors.toList());

                    return new FriendSuggestionResponse(
                            suggestion.getId(),
                            suggestion.getName(),
                            suggestion.getPhoto(),
                            commonSports.size(),
                            commonSports
                    );
                })
                .sorted((a, b) -> Integer.compare(b.mutualCount(), a.mutualCount())).collect(Collectors.toList());
    }
}