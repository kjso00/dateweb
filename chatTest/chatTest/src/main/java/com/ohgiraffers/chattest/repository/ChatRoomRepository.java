package com.ohgiraffers.chattest.repository;

import com.ohgiraffers.chattest.model.entity.ChatRoom;
import com.ohgiraffers.chattest.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByUser1AndUser2(User user1, User user2);
}
