package com.esporte.myapp.service;

import com.esporte.myapp.entity.Friendship;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.enums.FriendshipStatus;
import com.esporte.myapp.repository.FriendshipRepository;
import com.esporte.myapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createFriendship(User sender, User receiver) {
        Friendship newFriendship = new Friendship(sender, receiver);
        friendshipRepository.save(newFriendship);
        log.info("Amizade ATIVA criada entre '{}' e '{}'", sender.getId(), receiver.getId());
    }

    @Transactional
    public void removeFriendship(String currentUserId, String friendToRemoveId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Current user not found"));
        User friendToRemove = userRepository.findById(friendToRemoveId)
                .orElseThrow(() -> new EntityNotFoundException("Friend to remove not found"));

        Friendship friendship = friendshipRepository.findFriendshipBetween(currentUser, friendToRemove)
                .orElseThrow(() -> new EntityNotFoundException("Friendship not found"));

        if (friendship.getStatus() == FriendshipStatus.INACTIVE) {
            log.warn("Tentativa de desfazer amizade que já está INATIVA.");
            return;
        }

        friendship.setStatus(FriendshipStatus.INACTIVE);
        friendshipRepository.save(friendship);
        log.info("Amizade entre '{}' e '{}' foi desfeita (status INATIVO)", currentUserId, friendToRemoveId);
    }

    public List<User> findActiveFriends(User user) {
        return friendshipRepository.findFriendsOfUserByStatus(user, FriendshipStatus.ACTIVE);
    }
}