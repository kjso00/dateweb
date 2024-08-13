package com.ohgiraffers.chat.chat.service;

import com.ohgiraffers.chat.chat.model.entity.ChatMessage;
import com.ohgiraffers.chat.chat.model.entity.ChatRoom;

import com.ohgiraffers.chat.chat.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatUserRepository userRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    // 수정코드
//    public ChatRoom findChatRoomByUsers(Long userId1, Long userId2) {
//        // 두 사용자 간의 채팅방을 찾는 로직
//        return chatRoomRepository.findByUser1IdAndUser2IdOrUser1IdAndUser2Id(userId1, userId2, userId2, userId1);
//    }
    @Transactional
    public ChatRoom createChatRoom(Long userId1, Long userId2) {
        // 새 채팅방 생성 로직
        ChatRoom newRoom = new ChatRoom();
        newRoom.setUser1(userRepository.findById(userId1).orElseThrow());
        newRoom.setUser2(userRepository.findById(userId2).orElseThrow());
        return chatRoomRepository.save(newRoom);
    }

    // ------수정코드 8/2 15:33
    @Transactional
    public ChatRoom findChatRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId).orElse(null); // Optional을 사용하여 채팅방 조회
    }

    @Transactional
    public void saveMessage(ChatMessage chatMessage) {
        // 현재 시간을 LocalDateTime으로 설정
        LocalDateTime now = LocalDateTime.now();
        chatMessage.setTimestamp(now); // LocalDateTime 타입으로 설정
        ChatRoom chatRoom = chatMessage.getChatRoom();
        User sender = chatMessage.getSender();
        User recipient = (chatRoom.getUser1().getId().equals(sender.getId())) ? chatRoom.getUser2() : chatRoom.getUser1();
        chatMessage.setRecipient(recipient);
        // 메시지를 데이터베이스에 저장
        chatMessageRepository.save(chatMessage);
    }

    @Transactional
    public List<ChatMessage> getChatMessages(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        return chatMessageRepository.findByChatRoomOrderByTimestampAsc(chatRoom);
    }

    // 채팅방 나가기
    @Transactional
    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        chatMessageRepository.deleteByChatRoom(chatRoom);
        chatRoomRepository.delete(chatRoom);
    }
    @Transactional
    public ChatRoom getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
    }
    @Transactional
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    @Transactional
    public ChatRoom findOrCreateChatRoom(Long user1Id, Long user2Id) {
        User user1 = userRepository.findById(user1Id).orElseThrow(() -> new RuntimeException("User not found: " + user1Id));
        User user2 = userRepository.findById(user2Id).orElseThrow(() -> new RuntimeException("User not found: " + user2Id));

        // 항상 ID가 작은 사용자를 user1으로 설정
        if (user1.getId() > user2.getId()) {
            User temp = user1;
            user1 = user2;
            user2 = temp;
        }

        // 양방향으로 채팅방 검색
        ChatRoom chatRoom = chatRoomRepository.findByUser1AndUser2(user1, user2);
        if (chatRoom == null) {
            chatRoom = chatRoomRepository.findByUser1AndUser2(user2, user1);
        }

        if (chatRoom == null) {
            chatRoom = new ChatRoom(user1, user2);
            chatRoomRepository.save(chatRoom);
        }

        return chatRoom;
    }


}
