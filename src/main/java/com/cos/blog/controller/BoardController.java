package com.cos.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cos.blog.Service.BoardService;
import com.cos.blog.config.auth.PrincipalDetail;

@Controller
public class BoardController {
	
	
	@Autowired
	private BoardService boardService;
	
	
	//~~ index(@AuthenticationPrincipal PrincipalDetail principal){}
	// /WEB-INF/views/index.jsp를 찾아감 (prefix와 seffix)
	//	System.out.println("로그인 사용자 아이디 : "+principal.getUsername());		
	@GetMapping({"","/"})
	public String index(Model model,@PageableDefault(size = 3,sort = "id",direction = Sort.Direction.DESC) Pageable pageable ,@AuthenticationPrincipal PrincipalDetail principal) { //컨트롤러에서 세션을 어떻게 찾는지?
		model.addAttribute("boards",boardService.글목록(pageable));
		return "index"; //viewResolver가 작동이 되면 prefix+index+serfix 경로로 위의 데이터가 전달됨
	}
	
	@GetMapping("/board/{id}")
	public String findById(@PathVariable int id,Model model) {
		model.addAttribute("board",boardService.글상세보기(id));
		return "board/detail";
	}
	
	
	@GetMapping("board/{id}/updateForm")
	public String updateform(@PathVariable int id, Model model){
		model.addAttribute("board",boardService.글상세보기(id));
		
		return "board/updateForm";
	}

	//USER 권한이 있어야만 접근가능
	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "board/saveForm";
	}
	
}
