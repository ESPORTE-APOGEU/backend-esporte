package com.esporte.myapp.service;

import com.esporte.myapp.dto.FriendRequestResponse;
import com.esporte.myapp.dto.FriendRequestStatusDTO;
import com.esporte.myapp.entity.FriendRequest;
import com.esporte.myapp.entity.Friendship;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.enums.FriendshipStatus;
import com.esporte.myapp.enums.RequestStatus;
import com.esporte.myapp.repository.FriendRequestRepository;
import com.esporte.myapp.repository.FriendshipRepository;
import com.esporte.myapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j // Adiciona o logger
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final FriendshipService friendshipService;
    private final FriendshipRepository friendshipRepository; // << injeta

    public FriendRequestResponse createFriendRequest(String senderId, String receiverId) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new EntityNotFoundException("Sender not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new EntityNotFoundException("Receiver not found"));

        // 1) já são amigos?
        Optional<Friendship> existing = friendshipRepository.findFriendshipBetween(sender, receiver);
        if (existing.isPresent() && existing.get().getStatus() == FriendshipStatus.ACTIVE) {
            throw new IllegalStateException("Vocês já são amigos.");
        }

        // 2) existe pendente em qualquer direção?
        if (!friendRequestRepository.findPendingBetween(sender, receiver).isEmpty()) {
            throw new IllegalStateException("Já existe uma solicitação pendente entre vocês.");
        }

        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(RequestStatus.PENDING);

        FriendRequest savedRequest = friendRequestRepository.save(request);
        int mutualCount = friendshipRepository.countMutualFriendsByStatus(sender, receiver, FriendshipStatus.ACTIVE);
        return FriendRequestResponse.from(savedRequest, mutualCount);
    }

    @Transactional(readOnly = true)
    public FriendRequestStatusDTO getBetweenStatus(String meId, String otherId) {
        User me = userRepository.findById(meId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        User other = userRepository.findById(otherId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        boolean isFriend = friendshipRepository.findFriendshipBetween(me, other)
                .map(f -> f.getStatus() == FriendshipStatus.ACTIVE).orElse(false);

        var outgoing = friendRequestRepository.findPendingOutgoing(me, other);    // me -> other
        var incoming = friendRequestRepository.findPendingIncoming(me, other);    // other -> me

        boolean pendingOutgoing = outgoing.isPresent();
        boolean pendingIncoming = incoming.isPresent();
        Long reqId = outgoing.map(FriendRequest::getId).orElseGet(() -> incoming.map(FriendRequest::getId).orElse(null));

        return new FriendRequestStatusDTO(isFriend, pendingOutgoing, pendingIncoming, reqId);
    }


    public List<FriendRequestResponse> getPendingRequests(String receiverId) {
        log.info("Buscando solicitações pendentes para o receiver ID '{}'", receiverId);
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> {
                    log.error("Usuário (receiver) com ID '{}' não encontrado ao buscar solicitações pendentes.", receiverId);
                    return new EntityNotFoundException("Receiver not found");
                });

        List<FriendRequest> requests = friendRequestRepository.findByReceiverAndStatus(receiver, RequestStatus.PENDING);
        log.info("Encontradas {} solicitações pendentes para o usuário '{}'", requests.size(), receiverId);
        return requests.stream().map(req -> {
            int mutualCount = friendshipRepository.countMutualFriendsByStatus(
                    req.getSender(), receiver, FriendshipStatus.ACTIVE
            );
            return FriendRequestResponse.from(req, mutualCount);
        }).toList();
    }

    @Transactional
    public FriendRequestResponse respondToRequest(String currentUserId, Long requestId, RequestStatus status) {
        log.info("Processando resposta para a solicitação '{}' pelo usuário '{}' com status '{}'", requestId, currentUserId, status);
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.error("Solicitação de amizade com ID '{}' não encontrada.", requestId);
                    return new EntityNotFoundException("Request not found");
                });

        if (!request.getReceiver().getId().equals(currentUserId)) {
            log.warn("Tentativa não autorizada do usuário '{}' de responder à solicitação '{}', que pertence a '{}'",
                    currentUserId, requestId, request.getReceiver().getId());
            throw new IllegalStateException("User is not authorized to respond to this request");
        }

        log.info("Autorização confirmada. Atualizando status da solicitação '{}' para '{}'", requestId, status);
        request.setStatus(status);
        if (status == RequestStatus.ACCEPTED) {
            friendshipService.createFriendship(request.getSender(), request.getReceiver());
        }
        FriendRequest updatedRequest = friendRequestRepository.save(request);
        int mutualCount = friendshipRepository.countMutualFriendsByStatus(
                updatedRequest.getSender(), updatedRequest.getReceiver(),
                FriendshipStatus.ACTIVE
        );

        return FriendRequestResponse.from(updatedRequest, mutualCount);
    }
}