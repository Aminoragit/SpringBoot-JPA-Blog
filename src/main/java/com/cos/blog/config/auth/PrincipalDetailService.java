package com.cos.blog.config.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

@Service
public class PrincipalDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	// 스프링이 로그인을 요청을 가로챌때 username과 password변수를 가로채는데
	// password 부분처리는 알아서 하겠으니
	// username이 DB에 있는지만 확인해주면 되는데 그 확인을 아래 메서드에서 한다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User principal = userRepository.findByUsername(username).orElseThrow(() -> {
			return new UsernameNotFoundException("해당 사용자를 찾을수 없습니다" + username);
		});
		return new PrincipalDetail(principal); //로그인 실행시 loadUserByUsername이 실행되고 -> 패스워드는 알아서 하고
		//아이디가 있으면 PrincipalDetail에서 시큐리티의 세션에 유저정보가 저장된다. 그때의 타입은 UserDetails여야만 한다.
		//이처럼 오버라이드 구현을 안하면 우리가 쓰는 Username은 쓸수 없다.
		
		//즉, 맨 위에서 1) String username만 받아서 DB에 있는지 확인하고
		//2) 있으면 return을 하는데 PrincipalDetail에 현재 접속중인 user의 Data를 던져주면
		//3) 현재 유저의 session이 끝나기전까진(로그아웃) 세션이 유지되고
		//4) logout을 하면 현재 유저의 data를 캐시에서 지운다.
	}

}
