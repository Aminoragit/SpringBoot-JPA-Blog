package com.cos.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



//RestController는 문자 그자체를 return한다면
//Controller는 기본경로에 있는 파일을 return해줌

@Controller
public class TempControllerTest {
	
	// http://localhost:8000/blog/temp/home
	@GetMapping("/temp/home")
	public String tempHome() {
		System.out.println("tempHome()");
		//파일리턴 기본경로 : src/main/resources/static/파일
		//리턴명앞에 /을 붙여줘야함 안붙이면 ->.../resources/statichome.html이 된다
		return "/home.html";
	}
	
	@GetMapping("/temp/jsp")
	public String tempJps() {
		System.out.println("tempJSP()");
		return "test";
	}
	
	
}
