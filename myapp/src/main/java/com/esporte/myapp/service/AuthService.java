package com.esporte.myapp.service;

import com.esporte.myapp.dto.UserRequest;
import com.esporte.myapp.dto.UserResponse;
import com.esporte.myapp.entity.Sport;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.mapper.UserMapper;
import com.esporte.myapp.repository.SportRepository;
import com.esporte.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final SportRepository sportRepository;

    @Transactional
    public UserResponse register(UserRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        User user = new User();
        user.setId(request.id()); // ideal: já vem do jwt.subject (como você fez no controller)
        user.setName(request.name());
        user.setEmail(request.email());
        user.setBirthday(request.birthday());
        user.setGender(request.gender());
        user.setCity(request.city());
        user.setPhoto(request.photo()); // ✅ agora seta a photo

        // nomes -> entidades
        if (request.sports() != null) {
            user.setSports(resolveSportsByNames(request.sports()));
        }

        userRepository.save(user);
        return UserMapper.toResponse(user); // ✅ sempre use o mapper
    }

    private List<Sport> resolveSportsByNames(List<String> names) {
        if (names == null) return List.of();
        List<Sport> out = new ArrayList<>();
        for (String raw : names) {
            String name = Optional.ofNullable(raw).orElse("").trim();
            if (name.isEmpty()) continue;
            Sport sport = sportRepository.findByNameIgnoreCase(name)
                    .orElseGet(() -> sportRepository.save(Sport.builder().name(name).build()));
            out.add(sport);
        }
        // remove duplicados preservando a ordem
        return new ArrayList<>(new LinkedHashSet<>(out));
    }
}
