//// filepath: c:\Users\arthu\Documents\APOGEU_PROJECT\BTG\Esportes\backend-esporte\myapp\src\main\java\com\esporte\myapp\entity\FriendRequest.java
package com.esporte.myapp.entity;

import com.esporte.myapp.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "friend_requests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}