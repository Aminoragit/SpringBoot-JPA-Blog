package com.cos.blog.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//@Getter
//@Setter
//@Getter+@Setter = @Data
//@AllArgsConstructor //모든 생성자 
//@RequiredArgsConstructor //final이 붙은 애들만의 생성자

@Data 
@NoArgsConstructor
public class Member {
	private int id;
	private String username;
	private String password;
	private String email;
	
	
	@Builder //생성자를 굳이 다 쓸필요없이 m.build().객체로 생성가
	public Member(int id, String username, String password, String email) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}
	

}
