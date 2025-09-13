package com.esporte.myapp.service;

import com.esporte.myapp.dto.UserRequest;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User upsert(String subject, UserRequest req) {
        User u = userRepository.findById(subject).orElseGet(User::new);
        u.setId(subject); // SEMPRE o sub do JWT
        u.setName(req.name());
        u.setEmail(req.email());
        u.setBirthday(req.birthday());
        u.setGender(req.gender());
        u.setCity(req.city());
        u.setSports(req.sports());
        return userRepository.save(u);
    }

    public void delete(String id) {
        userRepository.deleteById(id);
    }
}
