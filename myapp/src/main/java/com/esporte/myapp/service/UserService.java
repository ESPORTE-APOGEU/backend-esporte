package com.esporte.myapp.service;

import com.esporte.myapp.dto.UserRequest;
import com.esporte.myapp.dto.UserResponse;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
    @Transactional(readOnly = true) // Boa prática adicionar em todos os métodos de leitura
    public List<User> getAll() {
        return userRepository.findAll();
    }
    public User upsert(String subject, UserRequest req) {
        User u = userRepository.findById(subject).orElseGet(User::new);
        u.setId(subject);
        u.setName(req.name());
        u.setEmail(req.email());
        u.setBirthday(req.birthday());
        u.setGender(req.gender());
        u.setCity(req.city());
        u.setSports(req.sports());
        if (hasText(req.photo())) { // <- só atualiza se veio algo
            u.setPhoto(req.photo());
        }
        return userRepository.save(u);
    }

    public void delete(String id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public UserResponse updateSports(String id, List<String> sportNames) {
        User u = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        u.setSports(sportNames);
        return toResponse(userRepository.save(u));
    }


    private UserResponse toResponse(User u) {

        return new UserResponse(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getCreatedAt(),  // se não existir no entity, troque/retire
                u.getBirthday(),
                u.getGender(),
                u.getCity(),
                u.getSports(),
                u.getPhoto()
        );
    }

    private static boolean hasText(String s) { return s != null && !s.isBlank(); }

}
