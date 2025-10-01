package com.esporte.myapp.repository;

import com.esporte.myapp.entity.FriendRequest;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiverAndStatus(User receiver, com.esporte.myapp.enums.RequestStatus status);
    List<FriendRequest> findBySender(User sender);

    @Query("select fr.sender from FriendRequest fr where fr.receiver = :user and fr.status = 'ACCEPTED' " +
           "union " +
           "select fr.receiver from FriendRequest fr where fr.sender = :user and fr.status = 'ACCEPTED'")
    List<User> findFriendsOfUser(@Param("user") User user);

    @Query("SELECT fr FROM FriendRequest fr WHERE (fr.sender = :user OR fr.receiver = :user) AND fr.status = 'PENDING'")
    List<FriendRequest> findPendingRequestsInvolvingUser(@Param("user") User user);

    @Query("""
      select fr from FriendRequest fr
       where ((fr.sender = :a and fr.receiver = :b) or (fr.sender = :b and fr.receiver = :a))
         and fr.status = 'PENDING'
    """)
    List<FriendRequest> findPendingBetween(@Param("a") User a, @Param("b") User b);

    @Query("""
      select fr from FriendRequest fr
       where fr.sender = :sender and fr.receiver = :receiver and fr.status = 'PENDING'
    """)
    Optional<FriendRequest> findPendingOutgoing(@Param("sender") User sender, @Param("receiver") User receiver);

    @Query("""
      select fr from FriendRequest fr
       where fr.sender = :other and fr.receiver = :me and fr.status = 'PENDING'
    """)
    Optional<FriendRequest> findPendingIncoming(@Param("me") User me, @Param("other") User other);
}