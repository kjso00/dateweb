package com.ohgiraffers.chat.login.admin.adminrepo;

import com.ohgiraffers.chat.login.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<UserEntity, Integer> {
}
