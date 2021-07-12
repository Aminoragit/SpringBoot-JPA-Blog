package com.cos.blog.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.cos.blog.Service.UserService;
import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class UserController {

	@Value("${cos.key}")
	private String cosKey;
	
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	// 아직 인증되지 않은 사용자들이 출입가능한 경로를 /auth/**로 허용하기
	// 그냥 주소가 /면 index.jsp 허용
	// static 이하에 있는 /js/**, /css/**, /image/**들은 허용가능하게 만들기

	// joinForm으로 이동
	@GetMapping("/auth/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}

	// 커스텀loginForm으로 이동
	@GetMapping("/auth/customLoginForm")
	public String customLoginForm() {
		return "user/customLogin";
	}

	// loginForm으로 이동
	@GetMapping("/auth/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}

	@GetMapping("/user/updateForm")
	public String updateForm() {
		return "user/updateForm";
	}

	@GetMapping("/auth/kakao/callback")
	// 데이터를 받는 컨트롤러 설정(ResponseBody
	public String kakaoCallback(String code) {
		// Post방식으로 key-value 데이터를 요청해야한다.
		// Retrofit2 <- 안드로이드에서 많이 사용
		// OkHttp
		// RestTemplate
		RestTemplate rt = new RestTemplate();

		// key-value 형식으로 하려고함
		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HttpBody 오브젝트 생성 (카카오한테 알려줄거)
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "51a8e33ebd36eacb05fe1cecf1974f61");
		params.add("redirect_uri ", "http://localhost:8000/auth/kakao/callback");
		params.add("code", code);

		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		// exchange메서드 -> HttpEntity라는 오브젝트를 담는 메서드임
		// Http 요청하기 -> POST방식 -> 결과는 response 변수의 응답 받음
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

		ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST,
				kakaoTokenRequest, String.class);

		// Gson,Json Simple,ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			// 파싱할때 OAuthToken의 이름이 다를경우 오류
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// @Getter @Setter or @Date 없으면 오류
			e.printStackTrace();
		}

		System.out.println("카카오 액세스 토큰 : " + oauthToken.getAccess_token());

		///// 위에서 받은 액세스 토큰으로 사용자 정보 조회////
		RestTemplate rt2 = new RestTemplate();
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

		ResponseEntity<String> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST,
				kakaoProfileRequest, String.class);
		// System.out.println(response2.getBody());

		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			// 파싱할때 OAuthToken의 이름이 다를경우 오류
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// @Getter @Setter or @Date 없으면 오류
			e.printStackTrace();
		}

		// 우리가 만든 User 오브젝트는 Id,email,role,c.Date,u.Date가 있는데
		// 카카오에서 전달해준 데이터를 우리 사이트에 저장할거임
		String kakaoUsername = kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId();
		System.out.println("블로그에 저장할 유저네임: " + kakaoUsername);

		String kakaoEmail = kakaoProfile.getKakao_account().getEmail();
		System.out.println("블로그에 저장할 이메일: " + kakaoEmail);

		
		//UUID -> 중복되지 않는 어떤 특정값을 랜덤하게 생성함
		UUID garbagePassword = UUID.randomUUID();
		String kakaoPassword = cosKey+kakaoProfile.getId()+kakaoProfile.getKakao_account().getEmail().toString();
		System.out.println("블로그에 저장할 패스워드: " + cosKey+kakaoProfile.getId());

		System.out.println("카카오 ID(번호) : " + kakaoProfile.getId());
		System.out.println("카카오 이메일 : " + kakaoProfile.getKakao_account().getEmail());

		User kakaoUser = User.builder()
				.username(kakaoUsername)
				.password(kakaoPassword)
				.email(kakaoEmail)
				.oauth("kakao")
				.build();

		// 가입자 혹은 비가입자인지 체크 해서 처리
		User originUser = userService.회원찾기(kakaoUser.getUsername());

		if (originUser.getUsername() == null) {
			// 비가입자면 바로 회원가입처리
			System.out.println("가입되지 않은 유저이므로 자동 회원가입됩니다.");
			userService.회원가입(kakaoUser);
		}

		// 가입자면 바로 로그인처리
		System.out.println("이미 가입된 인원이므로 자동으로 로그인을 진행합니다.");
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), kakaoPassword));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return "redirect:/";
	}
}
