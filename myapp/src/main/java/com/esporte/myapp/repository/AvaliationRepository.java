package com.esporte.myapp.repository;

import com.esporte.myapp.entity.Avaliation;
import com.esporte.myapp.entity.Event;
import com.esporte.myapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvaliationRepository extends JpaRepository<Avaliation, Long> {
    Optional<Avaliation> findByEventAndFromUserAndToUser(Event event, User fromUser, User toUser);
}
