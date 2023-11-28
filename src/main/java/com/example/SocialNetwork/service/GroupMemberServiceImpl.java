package com.example.SocialNetwork.service;

import com.example.SocialNetwork.entities.GroupMember;
import com.example.SocialNetwork.repository.GroupMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupMemberServiceImpl implements GroupMemberService{

    private final GroupMemberRepository memberRepository;

    public GroupMemberServiceImpl(GroupMemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    @Override
    public void saveGroupMember(GroupMember groupMember) {
        memberRepository.save(groupMember);
    }
    @Override
    public List<Long> getAllGroupMembers(Long id) {
        return memberRepository.findAllM(id);
    }


}
