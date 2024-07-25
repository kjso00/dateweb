package com.ohgiraffers.dateapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Queue를 사용했을때 예시
//@Service
//public class ChatService {
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    // 메시지 전송
//    public void sendMessage(String senderId, String receiverId, ChatMessage message) {
//        String queueKey = getQueueKey(senderId, receiverId);
//        redisTemplate.opsForList().rightPush(queueKey, message);
//    }
//
//    // 메시지 수신
//    public List<ChatMessage> receiveMessages(String senderId, String receiverId) {
//        String queueKey = getQueueKey(senderId, receiverId);
//        List<ChatMessage> messages = new ArrayList<>();
//        while (true) {
//            ChatMessage message = (ChatMessage) redisTemplate.opsForList().leftPop(queueKey);
//            if (message == null) break;
//            messages.add(message);
//        }
//        return messages;
//    }
//
//    // Queue 키 생성 (양방향 통신을 위해)
//    private String getQueueKey(String user1, String user2) {
//        return user1.compareTo(user2) < 0
//                ? "chat:" + user1 + ":" + user2
//                : "chat:" + user2 + ":" + user1;
//    }
//}
