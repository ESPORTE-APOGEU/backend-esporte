// Em: src/main/java/com/esporte/myapp/entity/Friendship.java
package com.esporte.myapp.entity;

import com.esporte.myapp.enums.FriendshipStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "friendships")
@IdClass(FriendshipId.class)
@Getter
@Setter
@NoArgsConstructor
public class Friendship {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id")
    private User user1;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id")
    private User user2;

    @Id
    @Enumerated(EnumType.ORDINAL)
    private FriendshipStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Friendship(User userA, User userB, FriendshipStatus status) {
        if (userA.getId().compareTo(userB.getId()) < 0) {
            this.user1 = userA;
            this.user2 = userB;
        } else {
            this.user1 = userB;
            this.user2 = userA;
        }
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }
    public Friendship(User userA, User userB) {
        if (userA.getId().compareTo(userB.getId()) < 0) {
            this.user1 = userA;
            this.user2 = userB;
        } else {
            this.user1 = userB;
            this.user2 = userA;
        }
        this.status = FriendshipStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }
}