package com.esporte.myapp.service;

import com.esporte.myapp.dto.*;
import com.esporte.myapp.entity.Sport;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.SportRepository;
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

    private final UserRepository repo;
    private final SportRepository sportRepo;

    @Transactional
    public UserResponse create(UserRequest req) {
        if (repo.existsById(req.id())) throw new IllegalArgumentException("ID já cadastrado");
        if (repo.existsByEmail(req.email())) throw new IllegalArgumentException("E-mail já cadastrado");

        User user = new User();
        user.setId(req.id());
        user.setName(req.name());
        user.setEmail(req.email());
        user.setBirthday(req.birthday());
        user.setGender(req.gender());
        user.setCity(req.city());
        user.setPhoto(req.photo());

        if (req.sports() != null) {
            user.setSports(resolveSportsByNames(req.sports()));
        }

        user = repo.save(user);
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse get(String id) {
        User u = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        return toResponse(u);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public void delete(String id) {
        if (!repo.existsById(id)) throw new EntityNotFoundException("Usuário não encontrado");
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
        if (req.photo() != null) u.setPhoto(req.photo());
        if (req.sports() != null) u.setSports(resolveSportsByNames(req.sports()));

        return toResponse(repo.save(u));
    }

    @Transactional
    public UserResponse updateSports(String id, List<String> sportNames) {
        User u = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        u.setSports(resolveSportsByNames(sportNames));
        return toResponse(repo.save(u));
    }

    private List<Sport> resolveSportsByNames(List<String> names) {
        if (names == null) return Collections.emptyList();
        List<Sport> result = new ArrayList<>();
        for (String raw : names) {
            String name = Optional.ofNullable(raw).orElse("").trim();
            if (name.isEmpty()) continue;
            Sport sport = sportRepo.findByNameIgnoreCase(name)
                    .orElseGet(() -> sportRepo.save(Sport.builder().name(name).build()));
            result.add(sport);
        }
        // opcional: remover duplicados preservando ordem
        return result.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new LinkedHashSet<>(result)), ArrayList::new));
    }

    private UserResponse toResponse(User u) {
        List<SportDTO> sports = u.getSports() == null ? List.of()
                : u.getSports().stream()
                .map(s -> new SportDTO(s.getId(), s.getName()))
                .toList();

        return new UserResponse(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getCreatedAt(),
                u.getBirthday(),
                u.getGender(),
                u.getCity(),
                sports,
                u.getPhoto()
        );
    }
}
