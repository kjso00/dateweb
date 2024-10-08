package com.ohgiraffers.chat.service;

import com.ohgiraffers.chat.model.dto.ChatMessageDTO;
import com.ohgiraffers.chat.model.dto.UserDTO;
import com.ohgiraffers.chat.model.entity.ChatRoom;
import com.ohgiraffers.chat.model.entity.ChatMessage;
import com.ohgiraffers.chat.model.entity.User;
import com.ohgiraffers.chat.repository.ChatMessageRepository;
import com.ohgiraffers.chat.repository.ChatRoomRepository;
import com.ohgiraffers.chat.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class ChatService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    // 수정코드
    public ChatRoom findChatRoomByUsers(Long userId1, Long userId2) {
        // 두 사용자 간의 채팅방을 찾는 로직
        return chatRoomRepository.findByUser1IdAndUser2IdOrUser1IdAndUser2Id(userId1, userId2, userId2, userId1);
    }

    public ChatRoom createChatRoom(Long userId1, Long userId2) {
        // 새 채팅방 생성 로직
        ChatRoom newRoom = new ChatRoom();
        newRoom.setUser1(userRepository.findById(userId1).orElseThrow());
        newRoom.setUser2(userRepository.findById(userId2).orElseThrow());
        return chatRoomRepository.save(newRoom);
    }

    // ------수정코드 8/2 15:33
    public ChatRoom findChatRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId).orElse(null); // Optional을 사용하여 채팅방 조회
    }


    public void saveMessage(ChatMessage chatMessage) {
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

    // 채팅방 나가기
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

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


}
