package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.helpercalsses.MyRequest;
import com.example.SocialNetwork.entities.GroupMember;
import com.example.SocialNetwork.entities.MembershipRequest;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.service.GroupMemberService;
import com.example.SocialNetwork.service.MembershipRequestService;
import com.example.SocialNetwork.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/groupmember")
public class GroupMemberController extends MyRequest {
    private final GroupMemberService memberService;
    private final MembershipRequestService requestService;
    private final UserService userService;

    public GroupMemberController(GroupMemberService memberService,
                                 MembershipRequestService requestService,
                                 UserService userService) {
        this.memberService =  memberService;
        this.requestService = requestService;
        this.userService = userService;
    }
    @PostMapping("/")
    public String approveRequest(@RequestBody MyRequest id) {
        MembershipRequest membershipRequest = requestService.getAllRequestsById(id.getId());
        if (membershipRequest == null)
            return null;

        User user = membershipRequest.getUser();
        SocialGroup socialGroup = membershipRequest.getSocialGroup();
        Date ldt = new Date();

        GroupMember groupMember = new GroupMember();
        groupMember.setDateJoined(ldt);
        groupMember.setSocialGroup(socialGroup);
        groupMember.setUser(user);

        memberService.saveGroupMember(groupMember);

        requestService.deleteRequestById(id.getId());
        return "Vas zahtev za uclanjenje grupe je prihvacen!";
    }


    @GetMapping("/allusersforgroup/{id}")
    public List<String> showAllUsersForGroup(@PathVariable Long id){
        List<String> users = new ArrayList<>();
        List<Long> groupMember = memberService.getAllGroupMembers(id);

        for (Long aLong : groupMember) {
            users.add(userService.findByID(aLong).getUsername());
        }
        return users;
    }
}

