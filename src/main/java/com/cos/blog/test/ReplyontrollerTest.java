package com.cos.blog.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.Board;
import com.cos.blog.repository.BoardRepository;

@RestController
public class ReplyontrollerTest {
	
	@Autowired
	private BoardRepository boardRepository;

	@GetMapping("/test/board/{id}")
	public Board getBoard(@PathVariable int id) {
		return boardRepository.findById(id).get(); //jackson 라이브러리 ( 오브젝트를 json으로 리턴) => 모델의 getter를 호출(Board.java의 변수들을 모두 호출하는건데 )
		//문제는 Board의 replys를 호출할때 Reply도 호출되는데 Reply안에는 Board가 있어서 또 Board롤 호출하고 또 Reply를 호출하고 == 무한참조 문제가 발생한다.
		//방지법 : 최초의 호출때만 부르면됨 -> JsonIgnoreProperty 어노테이션사용
	}
}
