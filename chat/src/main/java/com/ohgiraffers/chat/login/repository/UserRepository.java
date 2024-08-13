package com.ohgiraffers.chat.login.repository;

import com.ohgiraffers.chat.login.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserId(String userId); // id가 아닌 userId 기준으로 찾고 싶을때 쓸 수 있는 메서드
    UserEntity findByEmail(String email);
}