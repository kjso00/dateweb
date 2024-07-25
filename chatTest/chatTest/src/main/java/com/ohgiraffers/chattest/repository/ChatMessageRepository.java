package com.ohgiraffers.chattest.repository;

import com.ohgiraffers.chattest.model.entity.ChatRoom;
import com.ohgiraffers.chattest.model.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomOrderByTimestampAsc(ChatRoom chatRoom);
    void deleteByChatRoom(ChatRoom chatRoom);
}