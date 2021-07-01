package com.cos.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//사용자가 요청 => 응답(HTML파일)
//@Controller

// RestController란? == 사용자가 요청(get,post,put,delete)에 대한 응답을 해줌(Data)
@RestController
public class HttpControllerTest {
	
	private static final String TAG = "HttpControllerTest:";
	
	public String lombokTest() {
		Member m = new Member(1,"test","1234","email"); //AllargsContructor
		
		//@Builder
		Member m1 = Member.builder().username("test").password("1234").email("weqw@gmail.com").build();
		System.out.println(m1.getId()+","+	m1.getEmail());
		System.out.println(TAG+"getter:"+m.getId()); 
		m.setId(3000);
		System.out.println(TAG+"setter:"+m.getId());
		
		Member m2 = new Member();//NoArgsConstructer
		return "lombok 완료";

	}
	
	
	//인터넷 브라우저로 요청하는것은 get밖에 안된다(주소창에 복붙할때 getMapping만 사용가능 나머진(post,put,delete) 안됨)
	// http://localhost:8080/blog/http/get

	
	// 데이터를 JSON 형식으로 요청하려면 입력값을 @RequestBody로 설정하면 
	// 스프링의 MessageConverter가 알아서 JSON형식을 인식해서 get...을 해준다.
	//get의 경우는 body에 뭘 넣어서 보내줄 필요가 없으므로 @RequestBody를 안써도 됨
	
	
	//@RequestParam으로 할필요 없이 Object 형식{id=....}으로 보낼수 있음->자유로움
	@GetMapping("/http/get")
	//	public String getTest(@RequestParam int id,@RequestParam String username) {
	public String getTest(Member m) {
		return "get 요청 : "+m.getId()+", "+m.getUsername();
	}

	
	
	// http://localhost:8080/blog/http/post -> 405에러 뜰거임
	//사용하려면 postMan이나 Api Tester로 확인하는방법 밖에 없음
	@PostMapping("/http/post")
	public String postTest(@RequestBody Member m) {//Json형식으로 Post를 보내면 알아서 대입해주는걸 스프링부트의 MessageConverter가 자동으로 해주는거임
		return "post 요청 : "+m.getId()+", "+m.getUsername();
	}
	
	// http://localhost:8080/blog/http/put -> 405에러 뜰거임
	@PutMapping("/http/put")
	public String putTest(@RequestBody Member m) {
		return "put 요청 : "+m.getId()+", "+m.getUsername();
	}

	// http://localhost:8080/blog/http/delete -> 405에러 뜰거임
	@DeleteMapping("/http/delete")
	public String deleteTest() {
		return "delete 요청";
	}
	
	
}
