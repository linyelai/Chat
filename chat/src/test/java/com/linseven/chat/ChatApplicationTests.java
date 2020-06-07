package com.linseven.chat;

import com.linseven.chat.domain.Member;
import com.linseven.chat.mapper.MemberMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes={ChatApplication.class})
class ChatApplicationTests {

	@Autowired
	private MemberMapper memberMapper;

	@Test
	void contextLoads() {

		Member member = new Member();
		member.setUsername("18312483564");
		member.setPassword("123456");
		member.setNickname("18312483564");
		//int i = memberMapper.addMember(member);
		List<Member> memberList = memberMapper.findMembers("18312483564");

		assert(memberList!=null);
	}

}
