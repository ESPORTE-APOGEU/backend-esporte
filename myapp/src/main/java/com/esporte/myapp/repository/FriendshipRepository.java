package com.esporte.myapp.repository;

import com.esporte.myapp.entity.Friendship;
import com.esporte.myapp.entity.FriendshipId;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.enums.FriendshipStatus; // Importe o enum
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {

// REMOVA o método com CASE

    @Query("""
select f
from Friendship f
where (f.user1 = :user or f.user2 = :user)
  and f.status = :status
""")
    List<Friendship> findFriendshipsOfUserByStatus(
            @Param("user") User user,
            @Param("status") FriendshipStatus status
    );


    @Query("SELECT f FROM Friendship f WHERE (f.user1 = :userA AND f.user2 = :userB) OR (f.user1 = :userB AND f.user2 = :userA)")
    Optional<Friendship> findFriendshipBetween(@Param("userA") User userA, @Param("userB") User userB);

    @Query("SELECT f FROM Friendship f WHERE (f.user1.id IN :userIds OR f.user2.id IN :userIds) AND f.status = :status")
    List<Friendship> findAllActiveFriendshipsForUserIds(@Param("userIds") List<String> userIds, @Param("status") FriendshipStatus status);

    // FriendshipRepository.java
    @Query("""
select u
from User u
where exists (
  select 1 from Friendship f
  where f.status = :status
    and ( (f.user1 = :user1 and f.user2 = u) or (f.user2 = :user1 and f.user1 = u) )
)
and exists (
  select 1 from Friendship f
  where f.status = :status
    and ( (f.user1 = :user2 and f.user2 = u) or (f.user2 = :user2 and f.user1 = u) )
)
""")
    List<User> findMutualFriendsByStatus(
            @Param("user1") User user1,
            @Param("user2") User user2,
            @Param("status") FriendshipStatus status
    );

    @Query("""
select count(u)
from User u
where exists (
  select 1 from Friendship f
  where f.status = :status
    and ((f.user1 = :user1 and f.user2 = u) or (f.user2 = :user1 and f.user1 = u))
)
and exists (
  select 1 from Friendship f
  where f.status = :status
    and ((f.user1 = :user2 and f.user2 = u) or (f.user2 = :user2 and f.user1 = u))
)
""")
    int countMutualFriendsByStatus(
            @Param("user1") User user1,
            @Param("user2") User user2,
            @Param("status") FriendshipStatus status
    );

}