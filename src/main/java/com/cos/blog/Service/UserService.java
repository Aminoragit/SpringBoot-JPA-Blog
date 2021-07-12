package com.cos.blog.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

//이렇게 서비스로 설정한것들을 UserApiController에서 사용할거임
@Service // 스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌 == IoC를 해줌(==메모리에 대신 띄어줌)
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Transactional
	public User 회원찾기(String username) {
		User user = userRepository.findByUsername(username).orElseGet(() -> {
			return new User(); // 빈객체는 최소한 null은 아니다.
		});
		return user;
	}

	@Transactional // 회원가입 메서드가 1개의 트랜잭션이라고 알려줌
	public int 회원가입(User user) {
		String rawPassword = user.getPassword();
		String encPassword = encoder.encode(rawPassword);
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);
		try {
			userRepository.save(user); // 굳이 return 1을 안해줘도 GlobalException이 알아서 조정해줌
			return 1;
		} catch (Exception e) {
			e.getMessage();
			return -1;
		}
		// try {
		// userRepository.save(user);
		// return 1;
		// }catch(Exception e) {
		// e.printStackTrace();
		// System.out.println("UserService : 회원가입() : "+ e.getMessage());
		// }
		// return -1;
	}

	@Transactional
	public void 회원수정(User user) {
		// 뭔가를 수정할때는 영속화 컨텐스트에 User오브젝트를 영속화 시키고 영속화한걸 바꾸는게 좋다 -> 더티체킹
		User persistance = userRepository.findById(user.getId()).orElseThrow(() -> {
			return new IllegalArgumentException("회원찾기 실패");
		});

		// Validate 체크(oauth가 카카오나 구글이면) 비밀번호랑 이메일 수정을 원천 봉쇄하기
		if (persistance.getOauth() == null || persistance.getOauth().equals("")) {
			// Validate 체크 -> Oauth필드에 값이 없으면 수정가능
			String rawPassword = user.getPassword();
			String encPassword = encoder.encode(rawPassword);
			persistance.setPassword(encPassword);
			persistance.setEmail(user.getEmail());
		}

		// 회원수정 함수 종료 == 서비스 종료 == 트랜잭션 종료 == 자동 commit ==> 더티체킹
		// 굳이 save같은걸 할 필요가 없음
	}

	

//	@Transactional
//	public int 이메일체크(String email) {
//		try {	
//			int result = userRepository.existsCheck(email); // 굳이 return 1을 안해줘도 GlobalException이 알아서 조정해줌
//			return result;
//		} catch (Exception e) {
//			e.getMessage();
//			return -1;
//		}
//	}

}

// 이메일 중복확인 처리

//	@Transactional
//	public int 이메일체크(String email) {
//		boolean persistance = userRepository.findByEmail(email).equals(email);//입력값 email로 찾은 email값과 email이 동일한지 확인 boolean
//		if(persistance==false) {
//			//같지 않다.
//			System.out.println(persistance+"같지 않다.");
//			return 1;
//		}
//		System.out.println(persistance+"같다.");
//		return 0;
//	}

//	public static int userIdCheck(String user_id) {
//		boolean persistance = userRepository.findByUsername(user_id).isEmpty();
//		if(persistance != false) {
//		return 1;
// }
// return 0;
// }

// 전통적인 로그인의 서비스
// @Transactional(readOnly = true) // select할때 트랜잭션 시작, 서비스 종료시 트랜잭션 종료하면서 정합성을
// 유지하라는 설정
// public User 로그인(User user) {
// return userRepository.findByUsernameAndPassword(user.getUsername(),
// user.getPassword());
// }
