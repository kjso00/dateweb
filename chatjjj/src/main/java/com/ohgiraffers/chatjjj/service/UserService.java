package com.ohgiraffers.chatjjj.service;


import com.ohgiraffers.chatjjj.model.entity.Message;
import com.ohgiraffers.chatjjj.model.entity.User;
import com.ohgiraffers.chatjjj.repository.MessageRepository;
import com.ohgiraffers.chatjjj.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;


    @Transactional
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Transactional
    public List<User> findAllUsers() {
        return userRepository.findAll(); // 또는 적절한 사용자 목록을 반환하는 로직
    }

    @Transactional
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null); // 사용자 ID로 사용자 찾기
    }




}
