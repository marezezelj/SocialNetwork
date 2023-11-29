package com.example.SocialNetwork.configuration;

import com.example.SocialNetwork.entities.*;
import com.example.SocialNetwork.repository.*;
import com.example.SocialNetwork.repository.GroupMemberRepository;
import com.example.SocialNetwork.repository.MembershipRequestRepository;
import com.example.SocialNetwork.repository.SocialGroupRepository;
import com.example.SocialNetwork.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class DBSeeder implements CommandLineRunner {


    UserRepository userRepository;
    MembershipRequestRepository membershipRequestRepository;
    FriendsRepository friendsRepository;
    FriendRequestRepository friendRequestRepository;
    SocialGroupRepository socialGroupRepository;
    GroupMemberRepository groupMemberRepository;


    DBSeeder(UserRepository userRepository, MembershipRequestRepository membershipRequestRepository, SocialGroupRepository socialGroupRepository, FriendsRepository friendsRepository, FriendRequestRepository friendRequestRepository, GroupMemberRepository groupMemberRepository) {
        this.userRepository = userRepository;
        this.membershipRequestRepository = membershipRequestRepository;
        this.socialGroupRepository = socialGroupRepository;
        this.friendsRepository = friendsRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    private void seedUser(String username, String email, String password, boolean active, boolean admin) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setActive(active);
        user.setAdmin(admin);

        userRepository.save(user);
    }

    private void seedSocialGroup(String name, boolean type, int id) {
        SocialGroup socialGroup = new SocialGroup();
        socialGroup.setName(name);
        socialGroup.setType(type);
        List<User> adminUser = userRepository.findAll();
        socialGroup.setUser(adminUser.get(id));

        socialGroupRepository.save(socialGroup);
    }

    private void seedMembershipRequest(RequestStatus status, int id_user, int id_socialgroup) {
        List<User> users = userRepository.findAll();
        List<SocialGroup> socialGroups = socialGroupRepository.findAll();
        MembershipRequest membershipRequest = new MembershipRequest();
        membershipRequest.setRequestStatus(status);
        membershipRequest.setUser(users.get(id_user));
        membershipRequest.setSocialGroup(socialGroups.get(id_socialgroup));

        membershipRequestRepository.save(membershipRequest);
    }

    private void seedFriendRequest(RequestStatus requestStatus, Long user1Id, Long user2Id, Date date) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setStatus(requestStatus);
        friendRequest.setDate((java.sql.Date) date);
        friendRequest.setId_user1(user1Id);
        friendRequest.setId_user2(user2Id);
        friendRequestRepository.save(friendRequest);
    }

    private void seedFriends(Long user1Id, Long user2Id) {
        List<User> users = userRepository.findAll();
        Friends friends = new Friends();
        friends.setUser1Id(users.get(user1Id.intValue()));
        friends.setUser2Id(users.get(user2Id.intValue()));
        friendsRepository.save(friends);
    }

    private void seedGroupMember(int id_user, int id_socialgroup) {
        List<User> users = userRepository.findAll();
        List<SocialGroup> socialGroups = socialGroupRepository.findAll();
        GroupMember groupMember = new GroupMember();

        groupMember.setUser(users.get(id_user));
        groupMember.setDateJoined(new Date());
        groupMember.setSocialGroup(socialGroups.get(id_socialgroup));

        groupMemberRepository.save(groupMember);
    }


    @Override
    public void run(String... args) throws Exception {
        clearDatabase();

        seedUser("John", "john@example.com", "password1", true, true);
        seedUser("Alice", "alice@example.com", "password2", true, false);
        seedUser("Bob", "bob@example.com", "password3", true, false);
        seedUser("Eva", "eva@example.com", "password4", false, false);
        seedUser("Michael", "michael@example.com", "password5", false, false);

        seedSocialGroup("Group1", true,1);
        seedSocialGroup("Group2", false,1);
        seedSocialGroup("Group3", true,2);
        seedSocialGroup("Group4", false,2);
        seedSocialGroup("Group5", true,3);

        seedMembershipRequest(RequestStatus.PENDING,1,2);
        seedMembershipRequest(RequestStatus.ACCEPTED,1,1);
        seedMembershipRequest(RequestStatus.PENDING,1, 3);
        seedMembershipRequest(RequestStatus.REJECTED,2,4);
        seedMembershipRequest(RequestStatus.ACCEPTED,1,4);

        seedGroupMember(0,1);
        seedGroupMember(1,1);
        seedGroupMember(2,3);
        seedGroupMember(1,2);
        seedGroupMember(1,3);
        seedMembershipRequest(RequestStatus.PENDING, 1, 3);
        seedMembershipRequest(RequestStatus.ACCEPTED, 1, 2);
        seedMembershipRequest(RequestStatus.PENDING, 2, 3);
        seedMembershipRequest(RequestStatus.REJECTED, 2, 2);
        seedMembershipRequest(RequestStatus.ACCEPTED, 2, 4);

       //seedFriendRequest(RequestStatus.PENDING, 254L, 255L, new Date(System.currentTimeMillis()));
/*     seedFriendRequest(RequestStatus.ACCEPTED, 94L, 97L, new Date(System.currentTimeMillis()));
       seedFriendRequest(RequestStatus.PENDING, 94L, 98L, new Date(System.currentTimeMillis()));
*/
        seedFriends(2L, 1L);
        seedFriends(1L, 3L);
        seedFriends(2L, 3L);
    }

    private void clearDatabase() {
        this.groupMemberRepository.deleteAll();
        this.membershipRequestRepository.deleteAll();
        this.socialGroupRepository.deleteAll();
        this.userRepository.deleteAll();
        this.friendsRepository.deleteAll();
        this.friendRequestRepository.deleteAll();
    }
}
