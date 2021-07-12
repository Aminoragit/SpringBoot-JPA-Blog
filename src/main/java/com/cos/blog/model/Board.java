package com.cos.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity //Entity클래스다 == ORM클래스다 == 데이터에 매핑해주는 클래스다
public class Board {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	//내용에 섬머노트 라이브러리를 넣을건데(알아서 디자인해줌)
	//문제는 <html>형식이 들어감==용량이큼 
	//그래서 @Lob을 사용함
	@Lob //대용량 데이터
	private String content;

	//@ColumnDefault("0") //User.java의 String과 다르게 int라서 홑따옴표 필요없음
	
	private int count; //조회수
	
	
	//연관관계
	@ManyToOne(fetch=FetchType.EAGER) //다(Board)대일(User) 관계 1명의 유저는 여러개의 게시글을 쓸수 있다
	@JoinColumn(name="userId") //테이블 생성시 column명은 userId로 저장됨
	//위의 연관관계를 설정하고 아래처럼 User로 타입을 써주면 자동으로 FK가 생성됨 
	private User user; //DB는 오브젝트를 저장할수 없어서 FK를 쓰는데
	//자바는 오브젝트 저장이 가능해서 FK는 안쓰고 바로 테이블명을 사용가능함
	
	
	
	
	//mappedBy에는 Reply클래스의 Join~(name의 값이 아니라 필드명자체를 써주면된다.
	@OneToMany(mappedBy = "board",fetch = FetchType.EAGER)  //1개의 게시글에 여러개의 댓글을 쓸수 있음
	//FK가 필요없음(JoinColumn)
	//왜냐하면 DB에는 1정규화조건(원자성)을 위해 1개의 컬럼에는 1개의 data만 입력이 가능함
	//이걸 해결하기 위한게 mappedBy이다
	//mappedBy는? ==> 연관관계의 주인이 아니다,==난 FK가 아니니 DB에 컬럼을 만들지 마라
	@JsonIgnoreProperties({"board"})
	private List<Reply> replys; //Java.util의 리스트
	

	@CreationTimestamp
	private Timestamp createDate;

	@UpdateTimestamp
	private Timestamp currentUpdateDate;

		
}
