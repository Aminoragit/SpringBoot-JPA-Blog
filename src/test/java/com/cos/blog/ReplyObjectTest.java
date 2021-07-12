package com.cos.blog;



import org.junit.jupiter.api.Test;

import com.cos.blog.model.Reply;

public class ReplyObjectTest {
	
	@Test
	public void toString테스트() {
		Reply reply = Reply.builder().id(1).user(null).board(null).content("안녕").build();
		System.out.println(reply); // Object 출력시 toString 자동 호출됨
	}
}
