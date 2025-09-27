package com.esporte.myapp.entity;

import com.esporte.myapp.enums.FriendshipStatus;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FriendshipId implements Serializable {
    private String user1;
    private String user2;
    private FriendshipStatus status;
}