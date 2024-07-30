package com.ohgiraffers.dateapp.service;

import com.ohgiraffers.dateapp.model.entity.ChatMessage;
import com.ohgiraffers.dateapp.model.entity.ChatRoom;
import com.ohgiraffers.dateapp.model.entity.User;
import com.ohgiraffers.dateapp.repository.ChatMessageRepository;
import com.ohgiraffers.dateapp.repository.ChatRoomRepository;
import com.ohgiraffers.dateapp.repository.UserRepository;
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

    public void saveMessage(Long roomId, User sender, String content, Long recipientId) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRoomId(roomId);
        chatMessage.setSender(sender);
        chatMessage.setContent(content);
        chatMessage.setRecipientId(recipientId);
        // 현재 시간을 LocalDateTime으로 설정
        LocalDateTime now = LocalDateTime.now();
        chatMessage.setTimestamp(now); // LocalDateTime 타입으로 설정

        // 메시지를 데이터베이스에 저장
        chatMessageRepository.save(chatMessage);
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

    public ChatRoom getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
    }
}
