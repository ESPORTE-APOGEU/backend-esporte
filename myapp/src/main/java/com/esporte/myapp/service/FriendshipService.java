package com.esporte.myapp.service;

import com.esporte.myapp.dto.UserResponse;
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

    @Transactional(readOnly = true)
    public List<User> findActiveFriends(User user) {
        List<Friendship> edges = friendshipRepository
                .findFriendshipsOfUserByStatus(user, FriendshipStatus.ACTIVE);

        return edges.stream()
                .map(f -> f.getUser1().equals(user) ? f.getUser2() : f.getUser1())
                .toList();
    }


    @Transactional(readOnly = true)
    public List<UserResponse> getFriendsOf(String userId) {
        User me = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Busca TODAS as amizades ativas envolvendo o usuário
        var edges = friendshipRepository.findFriendshipsOfUserByStatus(me, FriendshipStatus.ACTIVE);

        // Para cada edge, pega “o outro” usuário e converte para DTO
        return edges.stream()
                .map(f -> {
                    User other = me.getId().equals(f.getUser1().getId()) ? f.getUser2() : f.getUser1();
                    return UserResponse.from(other);
                })
                .toList();
    }


}