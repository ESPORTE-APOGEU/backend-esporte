package com.esporte.myapp.repository;

import com.esporte.myapp.entity.FriendRequest;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiverAndStatus(User receiver, RequestStatus status);
    List<FriendRequest> findBySender(User sender);

    @Query("select fr.sender from FriendRequest fr where fr.receiver = :user and fr.status = 'ACCEPTED' " +
           "union " +
           "select fr.receiver from FriendRequest fr where fr.sender = :user and fr.status = 'ACCEPTED'")
    List<User> findFriendsOfUser(@Param("user") User user);
}