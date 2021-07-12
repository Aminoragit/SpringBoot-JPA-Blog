package com.cos.blog.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.dto.ResponseDto;

@ControllerAdvice //모든 패키지에서 Exception이 발생하면 이 패키지로 오도록
@RestController
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value =Exception.class) //IllegalArgumentException 발동시 이 메서드를 실행하라는 얘기
	public ResponseDto<String> handleArgumentException(Exception e) {
		return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
	}
	
	//IllegalArgumentException-> Exception을 하면 모든 Exception이 해당 메서드로 실행됨
	
	
}
