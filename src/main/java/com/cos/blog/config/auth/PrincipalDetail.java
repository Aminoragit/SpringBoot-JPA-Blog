package com.cos.blog.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.blog.model.User;

import lombok.Getter;

// 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면 UserDetails 타입의 오브젝트를
// 시큐리티의 고유한 세션 저장소에 저장을 해준다(UserDetails 타입만 저장가능한 공간이라 DB의 User를 UserDetails로 바꿀려고 하는거임
@Getter
public class PrincipalDetail implements UserDetails {

	private User user; // 컴포지션(객체를 품고있는 변수)

	public PrincipalDetail(User user) {
		this.user = user;
	}
	
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	// 계정이 만료되지 않았는지 리턴한다 (true:만료안됨)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정이 잠겨있는지 아닌지 확인후 리턴(true : 잠겨있지 않음)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 비밀번호가 만료됬는지 확인후 리턴(true:만료안됨)
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	// 계정이 활성화(사용가능)인지 확인(true:활성화)
	@Override
	public boolean isEnabled() {
		return true;
	}


	
	//계정이 갖고 있는 권한목록을 리턴한다 ( 권한이 여러개라면 루프를 돌리면된다)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collectors = new ArrayList<>();
		collectors.add(new GrantedAuthority() {
				
			@Override
			public String getAuthority() {
				return "ROLE_"+user.getRole(); //그냥 규칙임 ROLE_이 기본임, 그냥 USER만 보내면 인식을 못함
			}
		}); //자바는 오브젝트를 담을수 있지만 메서드만 넘길순없다 근데 메서드가 1개밖에 없으면 그냥 람다식으로 하는게 좋다.
		//collectors.add(()->{return "ROLE_"+user.getRole();});로 해줘도 된다.
		return collectors;
	}
}
