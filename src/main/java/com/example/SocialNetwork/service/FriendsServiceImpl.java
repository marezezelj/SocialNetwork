package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.FriendsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendsServiceImpl implements FriendsService {

    private FriendsRepository friendsRepository;

    public FriendsServiceImpl(FriendsRepository friendsRepository) {
        this.friendsRepository = friendsRepository;
    }

    @Override
    public void saveFriends(Friends friends) {
        friendsRepository.save(friends);
    }

    @Override
    public List<User> getFriendsByUser(Long userId) {
        return friendsRepository.getFriendsByUser(userId);
    }
}
