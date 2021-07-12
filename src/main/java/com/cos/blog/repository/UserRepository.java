package com.cos.blog.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cos.blog.model.User;

//해당 JpaRepository는 User테이블을 관리하는거고 User테이블의 PK는 Int 형식이다라는 의미
//DAO(Data Access Object)
//자동으로 bean등록이 된다 @Repository 생략가능
public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);

	User findByEmail(String email);

	@Query(value = "SELECT COUNT(*) FROM user where user.email = :email", nativeQuery = true)
	int existsCheck(@Param("email") String email);

}

//User findByUsernameAndPassword(String username, String password);

// 위에는 자동 아래는 정확하게 원하는것을 찾는 쿼리문이다.
// @Query(value = "SELECT * FROM user WHERE username=?1 AND password = ?2",
// nativeQuery = true)
// User login(String username,String password);