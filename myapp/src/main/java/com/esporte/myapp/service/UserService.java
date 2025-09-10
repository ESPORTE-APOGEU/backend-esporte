package com.esporte.myapp.service;

import com.esporte.myapp.dto.UserRequest;
import com.esporte.myapp.dto.UserResponse;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- IMPORTAR

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;

    // Métodos de escrita não precisam do readOnly = true
    @Transactional
    public UserResponse create(UserRequest req) {
        if (repo.existsById(req.id())) {
            throw new IllegalArgumentException("ID já cadastrado");
        }

        User user = new User();
        user.setId(req.id());
        user.setName(req.name());
        user.setEmail(req.email());
        user.setBirthday(req.birthday());
        user.setGender(req.gender());
        user.setCity(req.city());
        user.setSports(req.sports());

        user = repo.save(user);
        return toResponse(user);
    }

    // --- ADICIONE A ANOTAÇÃO AQUI ---
    @Transactional(readOnly = true)
    public UserResponse get(String id) {
        User u = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        return toResponse(u);
    }

    // --- E ADICIONE A ANOTAÇÃO AQUI ---
    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        List<User> users = repo.findAll();
        // A sessão agora fica aberta durante o .map(), resolvendo o problema!
        return users.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void delete(String id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado com o ID: " + id);
        }
        repo.deleteById(id);
    }

    private UserResponse toResponse(User u) {
        return new UserResponse(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getCreatedAt(),
                u.getBirthday(),
                u.getGender(),
                u.getCity(),
                u.getSports() // O acesso que causava o erro agora ocorre dentro da transação
        );
    }
}