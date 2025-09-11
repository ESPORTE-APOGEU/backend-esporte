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

    @Transactional
    public UserResponse update(String id, UserRequest req) {
        User u = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        if (req.name() != null) u.setName(req.name());
        if (req.email() != null) u.setEmail(req.email());
        if (req.birthday() != null) u.setBirthday(req.birthday());
        if (req.gender() != null) u.setGender(req.gender());
        if (req.city() != null) u.setCity(req.city());
        if (req.sports() != null) u.setSports(req.sports());
        if (req.photo() != null) u.setPhoto(req.photo());
        return toResponse(repo.save(u));
    }

    @Transactional
    public UserResponse updateSports(String id, List<String> sports) {
        User u = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        u.setSports(sports);
        return toResponse(repo.save(u));
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