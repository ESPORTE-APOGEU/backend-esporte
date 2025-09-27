package com.esporte.myapp.service;

import com.esporte.myapp.dto.FriendSuggestionResponse;
import com.esporte.myapp.entity.FriendRequest;
import com.esporte.myapp.entity.Friendship;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.enums.FriendshipStatus;
import com.esporte.myapp.repository.FriendRequestRepository;
import com.esporte.myapp.repository.FriendshipRepository;
import com.esporte.myapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Importa a anotação
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j // Adiciona o logger
public class FriendSuggestionService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipService friendshipService;
    private final FriendshipRepository friendshipRepository; // Necessário para a busca otimizada

    @Transactional(readOnly = true)
    public List<FriendSuggestionResponse> getFriendSuggestions(String userId) {
        log.info("Iniciando busca de sugestões para o usuário '{}'", userId);
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Usuário com ID '{}' não encontrado ao buscar sugestões.", userId);
                    return new EntityNotFoundException("User not found with ID: " + userId);
                });

        if (currentUser.getCity() == null || currentUser.getCity().isEmpty() || currentUser.getSports().isEmpty()) {
            log.warn("Usuário '{}' não possui cidade ou esportes para basear as sugestões. Retornando lista vazia.", userId);
            return List.of();
        }

        // --- Início da Lógica Refatorada ---

        // 1. Pega os amigos do usuário ATUAL e armazena seus IDs em um Set para busca rápida.
        List<User> currentUserFriends = friendshipService.findActiveFriends(currentUser);
        Set<String> currentUserFriendIds = currentUserFriends.stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        log.info("Usuário atual '{}' tem {} amigos.", userId, currentUserFriendIds.size());

        // 2. Monta a lista de IDs a serem excluídos da busca de sugestões.
        Set<String> excludedIds = new HashSet<>(currentUserFriendIds);
        excludedIds.add(currentUser.getId());

        List<FriendRequest> pendingRequests = friendRequestRepository.findPendingRequestsInvolvingUser(currentUser);
        pendingRequests.forEach(request -> {
            excludedIds.add(request.getSender().getId());
            excludedIds.add(request.getReceiver().getId());
        });
        log.info("Total de {} IDs (amigos + pendentes + próprio usuário) serão excluídos das sugestões.", excludedIds.size());

        log.info("Buscando sugestões no repositório para a cidade '{}' e esportes {}", currentUser.getCity(), currentUser.getSports());
        List<User> suggestedUsers = userRepository.findSuggestions(
                currentUser.getId(),
                currentUser.getCity(),
                currentUser.getSports(),
                excludedIds
        );
        if (suggestedUsers.isEmpty()) {
            log.info("Nenhuma sugestão de usuário encontrada após aplicar os filtros.");
            return List.of();
        }
        log.info("Repositório retornou {} usuários sugeridos.", suggestedUsers.size());

        // 4. OTIMIZAÇÃO: Busca todas as amizades de TODAS as sugestões em UMA ÚNICA QUERY.
        List<String> suggestionIds = suggestedUsers.stream().map(User::getId).collect(Collectors.toList());
        List<Friendship> friendshipsOfSuggestions = friendshipRepository.findAllActiveFriendshipsForUserIds(suggestionIds, FriendshipStatus.ACTIVE);

        // 5. Processa as amizades em um Mapa para acesso rápido: Map<UserID, Set<FriendID>>
        Map<String, Set<String>> suggestionsFriendMap = suggestionIds.stream()
                .collect(Collectors.toMap(Function.identity(), id -> new HashSet<>()));

        for (Friendship friendship : friendshipsOfSuggestions) {
            String user1Id = friendship.getUser1().getId();
            String user2Id = friendship.getUser2().getId();
            if (suggestionsFriendMap.containsKey(user1Id)) {
                suggestionsFriendMap.get(user1Id).add(user2Id);
            }
            if (suggestionsFriendMap.containsKey(user2Id)) {
                suggestionsFriendMap.get(user2Id).add(user1Id);
            }
        }

        // 6. Monta a resposta final, calculando APENAS os amigos em comum.
        List<FriendSuggestionResponse> finalSuggestions = suggestedUsers.stream()
                .map(suggestion -> {
                    // Pega o Set de amigos da sugestão (pré-carregado do mapa)
                    Set<String> suggestionFriendIds = suggestionsFriendMap.get(suggestion.getId());

                    // Faz a interseção: mantém apenas os IDs que existem em ambas as listas de amigos
                    // Usamos uma cópia para não modificar o mapa original
                    Set<String> mutualFriendIdsSet = new HashSet<>(suggestionFriendIds);
                    mutualFriendIdsSet.retainAll(currentUserFriendIds);

                    List<String> mutualFriendIdsList = new ArrayList<>(mutualFriendIdsSet);

                    // Cria o DTO simplificado focado em amigos em comum
                    return new FriendSuggestionResponse(
                            suggestion.getId(),
                            suggestion.getName(),
                            suggestion.getPhoto(),
                            mutualFriendIdsList.size(), // mutualCount agora é a contagem de amigos
                            mutualFriendIdsList       // A lista de IDs
                    );
                })
                // Ordena as sugestões pela quantidade de amigos em comum (do maior para o menor)
                .sorted(Comparator.comparing(FriendSuggestionResponse::mutualCount).reversed())
                .collect(Collectors.toList());

        log.info("Processamento finalizado. Retornando {} sugestões ordenadas.", finalSuggestions.size());
        return finalSuggestions;
    }
}