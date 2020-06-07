package com.linseven.chat.controller;

import com.linseven.chat.domain.Friend;
import com.linseven.chat.domain.FriendApply;
import com.linseven.chat.domain.Member;
import com.linseven.chat.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author linseven
 * @Date 2020/5/21
 */
@RestController
public class MemberController {

    @Autowired
    MemberMapper memberMapper;

    @PostMapping("/register")
    public int member(Member member){

      return   memberMapper.addMember(member);
    }

    @PostMapping("/findMembers")
    public List<Member> findMembers(String username){
        return memberMapper.findMembers(username);
    }
    @PostMapping("/addFriend")
    public int addFriend(FriendApply friendApply){
        return  memberMapper.addFriendApply(friendApply);
    }

    @PostMapping("/agreeFriendApply")
    public int agreeFriendApply(FriendApply friendApply)
    {

        if(friendApply.getStatus()==1) {
            Friend friend1 = new Friend();
            friend1.setUsername(friendApply.getUsername());
            friend1.setFriendUsername(friendApply.getFriendUsername());
            friend1.setAvatar("http://img0.imgtn.bdimg.com/it/u=401967138,750679164&fm=26&gp=0.jpg");
            memberMapper.addFriend(friend1);
            Friend friend2 = new Friend();
            friend2.setFriendUsername(friendApply.getUsername());
            friend2.setUsername(friendApply.getFriendUsername());
            friend2.setAvatar("http://img0.imgtn.bdimg.com/it/u=401967138,750679164&fm=26&gp=0.jpg");
            memberMapper.addFriend(friend2);
        }
        memberMapper.updateFriendApply(friendApply);
        return 0;
    }
}
