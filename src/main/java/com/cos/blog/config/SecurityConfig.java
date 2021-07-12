package com.cos.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.blog.config.auth.PrincipalDetailService;

//아래의 3개 어노테이션은 기본 세트라고 보면된다.

//설정파일의 Bean 등록(Bean등록 -- 스프링 컨테이너에서 객체를 관리할 수 있게 등록하는것)
@Configuration // Bean등록 (Ioc 관리)
@EnableWebSecurity // 시큐리티 필터 추가 = 활성화된 스프링 시큐리티 설정 중에 우리가 원하는 방식을 아래처럼 추가하겠다라는 얘기
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근하면 권한 및 인증을 미리 체크하겠단 얘기
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PrincipalDetailService principalDetailService;
	
	
	@Bean //어디서든 사용가능하게 만드는 어노테이션
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}


	@Bean //@Bean 추가시 IoC가 된다 == 아래의 BCryptPassword를 스프링에서 사용가능함
	public BCryptPasswordEncoder encodePWD() {
		//		String encPassword = new BCryptPasswordEncoder().encode("1234"); 해쉬화해주는 함수
		return new BCryptPasswordEncoder();
	}
	
	
	//시큐리티가 대신 로그인을 해주는데 -> password를 가로채기를 한다 -> 해당 패스워드가 뭘로 해쉬가 되서 회원가입이 되었는지 알아야
	//같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교를 할수 있다.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService) //우리가 로그인할때 Username을 확인하는 부분
		.passwordEncoder(encodePWD());//내가 사용한 인코더는 위의 인코다임을 알려줌
	}
	
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable() //csrf 토큰 비활성화(테스트할때는 걸어두는게 좋음)
		.authorizeRequests() // request가 들어오면
				.antMatchers("/","/auth/**","/js/**/","/css/**","/image/**","/images/**","/vendor/**","/fonts/**") // "/auth/**로 접근하는것들은 //주의할점 js도 해줘야 user.js에서 설정해준 click이벤트가 동작한다.
				.permitAll() // 모두 허용한다.
				.anyRequest() // 이외에는 모두
				.authenticated() // 인증이 되어있어야해
				.and().formLogin()
				.loginPage("/auth/loginForm") //인증이 필요하다면 모든 페이지 실행시 자동으로 이동
				.loginProcessingUrl("/auth/loginProc")  //스프링 시큐리티가 해당 주소로 요청오는 로그인을 가로채서 대신 로그인해준다. 
				.defaultSuccessUrl("/"); //로그인이 정상적으로 동작했드면 해당 주소로 이동 .failureUrl("")'<-실패시 주소		
	}
}
