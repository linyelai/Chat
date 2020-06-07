package com.linseven.chat.mapper;

import com.linseven.chat.domain.Friend;
import com.linseven.chat.domain.FriendApply;
import com.linseven.chat.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface MemberMapper {

     int addMember(Member member);
     int addFriendApply(FriendApply friendApply);
     int  addFriend(Friend friend);
     int updateFriendApply(FriendApply friendApply);

     List<Member> findMembers(String username);
}

