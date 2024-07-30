package com.ohgiraffers.dateapp.repository;

import com.ohgiraffers.dateapp.model.entity.ChatRoom;
import com.ohgiraffers.dateapp.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByUser1AndUser2(User user1, User user2);
}
