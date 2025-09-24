package com.esporte.myapp.service;

import com.esporte.myapp.dto.FriendRequestResponse;
import com.esporte.myapp.entity.FriendRequest;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.enums.RequestStatus;
import com.esporte.myapp.repository.FriendRequestRepository;
import com.esporte.myapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j // Adiciona o logger
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    public FriendRequestResponse createFriendRequest(String senderId, String receiverId) {
        log.info("Criando solicitação de amizade de '{}' para '{}'", senderId, receiverId);
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> {
                    log.error("Remetente (sender) com ID '{}' não encontrado.", senderId);
                    return new EntityNotFoundException("Sender not found");
                });
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> {
                    log.error("Destinatário (receiver) com ID '{}' não encontrado.", receiverId);
                    return new EntityNotFoundException("Receiver not found");
                });

        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(RequestStatus.PENDING);

        FriendRequest savedRequest = friendRequestRepository.save(request);
        log.info("Solicitação de amizade salva com ID '{}'", savedRequest.getId());
        return FriendRequestResponse.from(savedRequest);
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
        return requests.stream()
                .map(FriendRequestResponse::from)
                .collect(Collectors.toList());
    }

    public FriendRequestResponse respondToRequest(String currentUserId, String requestId, RequestStatus status) {
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
        FriendRequest updatedRequest = friendRequestRepository.save(request);
        return FriendRequestResponse.from(updatedRequest);
    }
}