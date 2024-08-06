package com.ohgiraffers.chatjjj.service;


import com.ohgiraffers.chatjjj.model.entity.Message;
import com.ohgiraffers.chatjjj.model.entity.User;
import com.ohgiraffers.chatjjj.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;


    @Transactional
    public void saveMessage(Message message) {

        messageRepository.save(message);
    }

    @Transactional
    public List<Message> getChatMessages(User user1, User user2) {
        return messageRepository.findBySenderAndRecipientOrRecipientAndSenderOrderByIdAsc(user1, user2, user2, user1);
    }


}

