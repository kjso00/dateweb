package com.ohgiraffers.chattest.repository;

import com.ohgiraffers.chattest.model.entity.ChatRoom;
import com.ohgiraffers.chattest.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByUser1IdAndUser2IdOrUser1IdAndUser2Id(Long user1Id, Long user2Id, Long user2Id2, Long user1Id2);
}
