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

    // 사용자와 상대방 채팅방 생성
//    public ChatRoom createChatRoom(String username1, String username2) {
//        User user1 = userRepository.findByUsername(username1);
//        User user2 = userRepository.findByUsername(username2);
//
//        ChatRoom existingRoom = chatRoomRepository.findByUser1AndUser2(user1, user2);
//        if (existingRoom != null) {
//            return existingRoom;
//        }
//
//        ChatRoom newRoom = new ChatRoom();
//        newRoom.setUser1(user1);
//        newRoom.setUser2(user2);
//        return chatRoomRepository.save(newRoom);
//    }

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


    // ------------------------------------
//    public void saveMessage(Long roomId, Long senderId, String content, Long recipientId) {
//        // ChatRoom 객체 가져오기
//        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
//                .orElseThrow(() -> new IllegalArgumentException("Chat room not found")); // 채팅방이 존재하지 않을 경우 예외 처리
//
//        ChatMessage chatMessage = new ChatMessage();
//        chatMessage.setRoomId(roomId);
//        chatMessage.setSenderId(senderId);
//        chatMessage.setContent(content);
//        chatMessage.setRecipientId(recipientId);
//        // 현재 시간을 LocalDateTime으로 설정
//        LocalDateTime now = LocalDateTime.now();
//        chatMessage.setTimestamp(now); // LocalDateTime 타입으로 설정
//        // 메시지를 데이터베이스에 저장
//        chatMessageRepository.save(chatMessage);
//    }

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


}
