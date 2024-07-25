package com.ohgiraffers.chattest.service;

import com.ohgiraffers.chattest.model.entity.ChatRoom;
import com.ohgiraffers.chattest.model.entity.ChatMessage;
import com.ohgiraffers.chattest.model.entity.User;
import com.ohgiraffers.chattest.repository.ChatMessageRepository;
import com.ohgiraffers.chattest.repository.ChatRoomRepository;
import com.ohgiraffers.chattest.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service

@Transactional
public class ChatService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatRoom createChatRoom(String username1, String username2) {
        User user1 = userRepository.findByUsername(username1);
        User user2 = userRepository.findByUsername(username2);

        ChatRoom existingRoom = chatRoomRepository.findByUser1AndUser2(user1, user2);
        if (existingRoom != null) {
            return existingRoom;
        }

        ChatRoom newRoom = new ChatRoom();
        newRoom.setUser1(user1);
        newRoom.setUser2(user2);
        return chatRoomRepository.save(newRoom);
    }

    public void saveMessage(Long chatRoomId, String senderUsername, String content) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        User sender = userRepository.findByUsername(senderUsername);

        ChatMessage message = new ChatMessage();
        message.setChatRoom(chatRoom);
        message.setSender(sender);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        chatMessageRepository.save(message);
    }

    public List<ChatMessage> getChatMessages(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        return chatMessageRepository.findByChatRoomOrderByTimestampAsc(chatRoom);
    }

    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        chatMessageRepository.deleteByChatRoom(chatRoom);
        chatRoomRepository.delete(chatRoom);
    }
}
