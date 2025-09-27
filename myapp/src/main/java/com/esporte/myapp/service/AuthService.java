package com.esporte.myapp.service;

import com.esporte.myapp.dto.AuthRequest;
import com.esporte.myapp.dto.AuthResponse;
import com.esporte.myapp.dto.UserRequest;
import com.esporte.myapp.dto.UserResponse;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public UserResponse register(UserRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        User user = new User();
        user.setId(request.id()); // ID do Clerk
        user.setName(request.name());
        user.setEmail(request.email());
        user.setBirthday(request.birthday());
        user.setGender(request.gender());
        user.setCity(request.city());
        user.setSports(request.sports());

        userRepository.save(user);

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getBirthday(),
                user.getGender(),
                user.getCity(),
                user.getSports(),
                user.getPhoto(),
                user.getTotalSkill(),
                user.getTotalRating(),
                user.getTotalReceivedEvaluations()
        );
    }
}
