package com.example.security1.repository;


import com.example.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


//CRUD 함수를 JpaRepository가 들고 있음.
//@Repository라는 어노테이션이 없어도 IoC 가능. 이유는 JpaRepository를 상속했기 때문
public interface UserRepository extends JpaRepository<User, Integer> {
    //findBy 까진 규칙 -> Username은 문법
    //select * from user where username = username

    //select * from user where username=1?
    Optional<User> findByUsername(String username);

    //public User findByUsername(String username);    //궁금하면 Jpa query method 검색해봐

}