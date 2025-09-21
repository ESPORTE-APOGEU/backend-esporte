package com.esporte.myapp.repository;

import com.esporte.myapp.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // "User_Id" navega para o id do usuário (String)
    List<Notification> findByUser_IdOrderByTimestampDesc(String userId);

    Optional<Notification> findByIdAndUser_Id(Long id, String userId);
}
