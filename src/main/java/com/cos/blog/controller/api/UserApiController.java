package com.cos.blog.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.Service.UserService;
import com.cos.blog.dto.ReplySaveRequestDto;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.User;

@RestController // data만 return할거라 rest
public class UserApiController {

	@Autowired // DI
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

//	@Autowired
//	private HttpSession session;

	@PostMapping("/auth/joinProc")
	public @ResponseBody ResponseDto<Integer> save(@RequestBody User user) { // username, password,email
		// 실제로 DB에 insert를 하고 아래에서 return이 실행되면된다.
		System.out.println("UserApiController.save() 호출됨");
		userService.회원가입(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}


	
	
	

	// @RequestBody->자체적으로 데이터를 Json으로 바꿔줌
	@PutMapping("/user")
	public ResponseDto<Integer> update(@RequestBody User user) {
		userService.회원수정(user);

		// -----------세션 강제 등록 회원수정 이후 변경된 요소로 자동 재로그인하기 위해서 세션 강제생성임------------
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		// ---------------------------------

		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

	/////////// 회원가입을 활용한 이메일 중복 체크하기////////////////

	/////////////////////////////////////////////////////////////

	// 왜 시큐리티 사용시 login mapping을 안해줌?? - 시큐리티가 알아서 가로챔

	// 정통적인 방식의 로그인
	// @PostMapping("/api/user/login")
	// public ResponseDto<Integer> login(@RequestBody User user,HttpSession
	// session){
	// System.out.println("UserApiController : login 호출됨");
	// User principal = userService.로그인(user); //principal(접근주체)
	// if(principal != null) {
	// session.setAttribute("principal", principal);
	// }
	// return new ResponseDto<Integer>(HttpStatus.OK, 1);
	// }

}
