package com.example.SocialNetwork.service;

import com.example.SocialNetwork.dto.FriendRequestDTO;
import com.example.SocialNetwork.entities.FriendRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FriendRequestService {
    ResponseEntity<Object> sendFriendRequest(FriendRequest friendRequest);

    List<FriendRequestDTO> getAllRequests(Long id);

    ResponseEntity<Object> respondToRequest(Long id, Long status);
}
