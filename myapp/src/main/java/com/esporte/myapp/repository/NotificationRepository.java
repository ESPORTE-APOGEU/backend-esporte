package com.esporte.myapp.repository;

import com.esporte.myapp.entity.Notification;
import com.esporte.myapp.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // "User_Id" navega para o id do usuário (String)
    List<Notification> findByUser_IdOrderByTimestampDesc(String userId);

    Optional<Notification> findByIdAndUser_Id(Long id, String userId);

    // NotificationRepository.java
    List<Notification> findByUser_IdAndStatusInOrderByTimestampDesc(String userId,
                                                                    Collection<NotificationStatus> statuses);

    List<Notification> findByRelatedEntry_IdAndType(Long entryId, String type);

    List<Notification> findByUser_IdAndStatusNotOrderByTimestampDesc(String userId,
                                                                     NotificationStatus status);

}
