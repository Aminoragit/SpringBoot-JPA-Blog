package com.cos.blog.test;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

@RestController //html이 아닌 data를 리턴해주는것이 RestController
public class DummyControllerTest {
	
	@Autowired //UserRepository타입으로 메모리에 떠있음 == 의존성 주입(DI)
	private UserRepository userRepository;

	
	
	
	
	
	//{id} 주소로 파라메터를 전달받을수 있음
	//http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		//Repository의 데이터는 Optional타입이다 왜냐하면
		//DB에서 id를 기준으로 찾았는데 데이터가 없으면 null이 return되는데
		//그럴 경우를 방지하기 위해 Optional로 User객체를 감싸서 가져올것이니
		//null인지 아닌지 판단해서 return하라는것이다
		//아니야 절대 이건 null일수가 없어일경우 뒤에 .get()을 붙이면된다.
		//		userRepository.findById(id).get(); 당연히 위험함
		// orElseGet(Supplier 타입) -> get() 오버라이드 ->return new User()로 해주면;
		//에러페이지는 아니고 null인값이 출력됨
		
		//만약 없는 유저라면 빈 객체를 user에 넣어줌으로서 null은 아니게 만들수 있음
		
		//orElseThrow-> IllegalArgumentException로 해주는게 더 좋음
		//람다식
		//		new Supplier<IllegalArgumentException>() {
		//			@Override	
		//			public IllegalArgumentException get() {
		//				return new IllegalArgumentException("해당 유저는 없습니다. id: "+id);
		//			}		
		//		}
		
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다. id: "+id));
		return user;
	}
	
	//findByEmail
	@GetMapping("/get/{email}")
	public User showByEmail(@PathVariable String email) {
	    return userRepository.findByEmail(email);
	}
	
	
	
	//http://localhost:8000/blog/dummy/join (요청)
	//http의 Body에 userName,password,email데이터를 가지고 요청하면 자동으로 key:value형태로 만들어줌
	@PostMapping("/dummy/join")
//	public String join(String userName, String password, String email) {
	public String join(User user) {
		System.out.println("id:"+user.getId());
		System.out.println("role: "+user.getRole());
		System.out.println("createDate: "+user.getCreateDate());
		System.out.println("username : "+user.getUsername());
		System.out.println("password : "+user.getPassword());
		System.out.println("email : "+user.getEmail());
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입 완료";
		
	}
	
	@GetMapping("/dummy/users")
	public List<User> list(){
		return userRepository.findAll();
	
	}
	
	
	
	//Page<User>로 해주고
	//Page<User> users = userRepository.findAll(pageable)로 해주면 데이터뿐만아니라 전체 페이지수와 페이지의 여러정보들도 같이 반환해준다
	//이를 이용해서 페이지 이동 버튼을 total페이지만큼 만들수도 있다.
	
	//1페이지당 2건 의 데이터를 리턴받기
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size = 2,sort = "id",direction = Sort.Direction.DESC) Pageable pageable){
		Page<User> pagingInfo = userRepository.findAll(pageable);

		//분기처리도 가능함
		//		if(pagingInfo.isFirst()) {			
		//		}
		
		List<User> users = userRepository.findAll(pageable).getContent();
		return users;
	}
	
	
	//데이터 업데이트 하기
	//Json형태로 데이터를 받으려면 @RequestBody로 해줘야한다.
	//Mapping의 타입이 다르면 주소가 중복되도 알아서 구분한다.
	@PutMapping("/dummy/user/{id}")
	@Transactional //save를 하지 않아도 DB에 업데이트가 된다. == 더티체킹
	public User updateUser(@PathVariable int id,@RequestBody User requestUser) {
		
		System.out.println("id : "+ id);
		System.out.println("password: "+requestUser.getPassword());
		System.out.println("email: "+requestUser.getEmail());
		
		
		//자바는 함수파라미터에 함수를 바로 넣을수는 없고 람다식으로 넣을수는 있다
		//Dart나 JS는 가능함
		
		//1) 먼저 해당 아이디가 있는지찾고
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패하였습니다.");
		});
		
		//2) 있을경우
		//2-1) user에 원래 있던 user[userId]의 데이터를 넣고
		//2-2) user의 기존값을 setter로 변경하면
		//2-3) 원하는것만 변경 + null 에러를 방지 할수 있다.
		//2-4) 이후 save로 DB에 저장하는거임.
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
				
		//save()는 ID가 없으면 insert를
		//ID를 전달하면 id에 대한 데이터가 있으면 update를,
		//id에 대한 데이터가 없으면 insert를 한다.
		// userRepository.save(user);
		return user;
	}
	
	
	//데이터 삭제하기
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
			
		} catch (EmptyResultDataAccessException e) {
			return "삭제 실패 해당 ID는 DB에 없습니다";
		}

		return "삭제완료 id: "+id;
	}
	
	
}
