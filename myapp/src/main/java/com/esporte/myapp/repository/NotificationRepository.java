// src/main/java/com/esporte/myapp/repository/NotificationRepository.java
package com.esporte.myapp.repository;

import com.esporte.myapp.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser_IdOrderByTimestampDesc(Long userId);
    Optional<Notification> findByIdAndUser_Id(Long id, Long userId);
    List<Notification> findByUserId(Long userId);
}
