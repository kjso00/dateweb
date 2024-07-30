package com.ohgiraffers.dateapp.service;

import com.ohgiraffers.dateapp.model.entity.User;
import com.ohgiraffers.dateapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // 실제 구현시 비밀번호 암호화 필요
        return userRepository.save(user);
    }

    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) { // 실제 구현시 암호화된 비밀번호 비교 필요
            return user;
        }
        return null;
    }
}
