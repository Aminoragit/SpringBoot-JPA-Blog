package com.cos.blog.model;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//시작하기전 application.yml ddl-auto: create인지 확인해야함
//처음 DB를 만들때만 create로 하고 이후에는 update로 바꿔줘야 한다.


//ORM -> JAVA뿐만 아니라 모든 언어들의 Ojbect를 테이블로 매핑해줌(업데이트나 생성을 알아서 해줌)


@Data //getter setter
@NoArgsConstructor //빈생성자
@AllArgsConstructor //전체생성자
@Builder //선택생성자
@Entity //Spring 실행시 연결된 mysql에 클래스를 Table화 해서 자동 입력해준다 
// @DynamicInsert Insert시 기본값 입력
public class User {

	
	@Id //Primary Key
	@GeneratedValue(strategy = GenerationType.IDENTITY)//IDENTITY 전략 == 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다. => 
	//mysql이면 AI를 오라클이면 시퀸스를 해준다는 얘기 알아서 자동으로 결정해줌
	private int id; //시퀀스(오라클), auto_increament(mysql)

	@Column(nullable=false, length = 30,unique=true) //길이제한과 Notnull설정
	private String username; 
	
	@Column(nullable=false, length = 100) //왜이렇게 크게 했냐면 비밀번호를 해쉬로 변경해서 암호화할거고 암호화한걸 DB에 넣을것이므로 일부러 크게 한거임
	private String password;

	@Column(nullable=false, length = 50)
	private String email;

	//(admin인지 user인지 manager인지 권한을 주는것)
	//@ColumnDefault("'user'") //특이하게 " ' user ' "로 해야한다 <- 문자라고 알려주는것
	//DB는 RoleType이란게 없으므로 @Enumerated(EnumType.STRING)이라고 Enum인것을 알려줘야한다.
	@Enumerated(EnumType.STRING)
	private RoleType role; //Enum을 쓰는게 좋다(도메인==범위) 데이터에 도메인 연결이 가능. String은 오타가 날수 있음<= 
	
	@CreationTimestamp //시간이 자동입력됨(insert될때 시간)
	private Timestamp createDate;
	
	private String oauth; // google , kakao Auth기능구현
	
	@UpdateTimestamp
	private Timestamp currentUpdateDate;

	
}
