package com.cos.blog.Service;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.dto.ReplySaveRequestDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;
import com.cos.blog.repository.UserRepository;

@Service
public class BoardService {

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private ReplyRepository replyRepository;

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public void 글쓰기(Board board, User user) {
		// user 정보를 가져와야 하는데 BoardService에서 PrintcialDetail에서 가져오게하기
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);
	}

	// findAll은 말그대로 다 가져오는데 페이징도 가져온다.
	@Transactional(readOnly = true)
	public Page<Board> 글목록(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public Board 글상세보기(int id) {
		return boardRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("글 상세보기 실패 : 해당 아이디가 존재하지 않습니다.");
		});
	}

	@Transactional
	public void 삭제하기(int id) {
		System.out.println("글삭제하기 " + id);
		boardRepository.deleteById(id);
	}

	@Transactional
	public Response 글수정하기(int id, Board requestboard, User user) throws Exception {
		System.out.println("글 수정하기" + id);
		Board board = boardRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("글 찾기 실패 : 해당 아이디가 존재하지 않습니다.");
		}); // 영속화 완료

		System.out.println("현재 접속중인 user의 userId : " + user.getId());
		System.out.println("-----------------------");
		System.out.println(boardRepository.findById(id).get().getUser().getId());// 게시글을 작성한 user의 id가 나옴
		System.out.println("-----------------------");

		if (user.getId() != boardRepository.findById(id).get().getUser().getId()) {
			throw new Exception("해당 게시글의 작성자가 아닙니다.");
		} else {
			Response res = new Response();
			board.setTitle(requestboard.getTitle());
			board.setContent(requestboard.getContent());
			// 해당 함수로 종료시(==서비스 종료)될때 트랜잭션이 종료되면서 더티체킹이 일어나면서 자동 update가 된다. ==db flush
			return res;
		}
	}

	// DTO(Data TransFer Object)의 장점 => 일일히 1개씩 보낼 필요없이 내가 필요한 데이터를 한번에 받아서 날릴수
	// 있다(그냥 일괄적용이라고 보면됨)
	@Transactional
	public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto) {
		// ReplyRepository에서 @Query 설정한것을 바로 넣으면 된다.
		int result = replyRepository.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(),
				replySaveRequestDto.getContent());
		System.out.println(result); //Object를 출력하게 되면 자동으로 toString()이 호출된다-> 그래서 Reply.java에 toString을 override함
		// Board board =
		// boardRepository.findById(replySaveRequestDto.getBoardId()).orElseThrow(() ->
		// {
//			return new IllegalArgumentException("댓글 작성 실패 : 게시글 아이디가 존재하지 않습니다.");
//		});
//
//		User user = userRepository.findById(replySaveRequestDto.getUserId()).orElseThrow(() -> {
//			return new IllegalArgumentException("댓글 작성 실패 : 유저 아이디가 존재하지 않습니다.");
//		});

		// 데이터 입력 방법1
		// Reply reply = Reply.builder()
		// .user(user)
		// .board(board)
		// .content(replySaveRequestDto.getContent())
		// .build();

		// 2. 방법2
		// Reply.java에 update함수를 추가하면 아래처럼 작성가능
		// 아니면 그냥 위에거 그대로 사용해도 된다.
		// Reply reply = new Reply();
		// reply.update(user, board, replySaveRequestDto.getContent());

		// requestReply.setUser(user);
		// requestReply.setBoard(board);
		// replyRepository.save(reply);

	}
}
