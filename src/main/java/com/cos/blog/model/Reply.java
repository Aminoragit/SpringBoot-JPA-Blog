package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false,length = 200)
	private String content;
	
	//누가 어느 테이블에 썼는지가 필요함
	@ManyToOne //여러개의 답변은 1개의 게시글에 존재할수 있다.
	//OnetoOne이면 1개의 게시글에 1개만 가능
	//OnetoMany 여러개의 게시글에 1개의 답변만 가능
	@JoinColumn(name = "boardId")
	private Board board;
	
	@ManyToOne//1나의 유저는 여러개의 답변을 달수 있다.
	@JoinColumn(name = "userId")
	private User user;
	

	@CreationTimestamp
	private Timestamp createDate;
	
	private Timestamp updateDate;
}













