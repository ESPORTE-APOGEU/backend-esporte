package com.esporte.myapp.service;

import com.esporte.myapp.dto.FriendSuggestionResponse;
import com.esporte.myapp.entity.FriendRequest;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.FriendRequestRepository;
import com.esporte.myapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Importa a anotação
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j // Adiciona o logger
public class FriendSuggestionService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Transactional(readOnly = true)
    public List<FriendSuggestionResponse> getFriendSuggestions(String userId) {
        log.info("Iniciando busca de sugestões para o usuário '{}'", userId);
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Usuário com ID '{}' não encontrado ao buscar sugestões.", userId);
                    return new EntityNotFoundException("User not found with ID: " + userId);
                });

        if (currentUser.getCity() == null || currentUser.getCity().isEmpty() || currentUser.getSports().isEmpty()) {
            log.warn("Usuário '{}' não possui cidade ou esportes cadastrados. Retornando lista vazia de sugestões.", userId);
            return List.of();
        }

        Set<String> excludedIds = new HashSet<>();
        excludedIds.add(currentUser.getId());
        log.debug("ID do usuário atual '{}' adicionado à lista de exclusão.", currentUser.getId());

        List<User> friends = friendRequestRepository.findFriendsOfUser(currentUser);
        friends.forEach(friend -> excludedIds.add(friend.getId()));
        log.info("Encontrados {} amigos para excluir das sugestões.", friends.size());

        List<FriendRequest> pendingRequests = friendRequestRepository.findPendingRequestsInvolvingUser(currentUser);
        pendingRequests.forEach(request -> {
            excludedIds.add(request.getSender().getId());
            excludedIds.add(request.getReceiver().getId());
        });
        log.info("Encontradas {} solicitações pendentes envolvendo o usuário para excluir das sugestões.", pendingRequests.size());
        log.debug("Total de {} IDs para excluir: {}", excludedIds.size(), excludedIds);

        log.info("Buscando sugestões no repositório para a cidade '{}' e esportes {}", currentUser.getCity(), currentUser.getSports());
        List<User> suggestedUsers = userRepository.findSuggestions(
                currentUser.getId(),
                currentUser.getCity(),
                currentUser.getSports(),
                excludedIds
        );
        log.info("Repositório retornou {} usuários sugeridos.", suggestedUsers.size());

        Set<String> currentUserSportsSet = new HashSet<>(currentUser.getSports());

        List<FriendSuggestionResponse> finalSuggestions = suggestedUsers.stream()
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
                .sorted((a, b) -> Integer.compare(b.mutualCount(), a.mutualCount()))
                .collect(Collectors.toList());

        log.info("Processamento finalizado. Retornando {} sugestões ordenadas.", finalSuggestions.size());
        return finalSuggestions;
    }
}