package com.cos.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//절대 test Package를 만들때 동일한경로.test로 설정해줘야한다.
//com.cos.test로 하면 동작하지 않음
//이유) 스프링 -> IOC(제어의 역전) -> 스프링에서는 new로 객체를 안만들어도 알아서 해줌
//왜 IOC? ) 레퍼런스변수를 스프링이 관리 + 싱글톤 패턴

//스프링이 com.cos.blog 패키지 이하를 스캔해서 모든 파일을 메모리에 new하는건 아니고
//특정 어노테이션이(@)이 붙어있는 클래스 파일들을 new 해서(IOC) 스프링 컨테이너에  관리해준다.
@RestController 
public class BlogControllerTest {
	
	
	//http://localhost:8080/test/hello
	@GetMapping("/test/hello")
	public String hello() {
		return "<h1>hello spring boot</h1>";
	}
	
}
