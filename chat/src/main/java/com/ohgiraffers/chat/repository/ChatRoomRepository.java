package com.ohgiraffers.chat.repository;


import com.ohgiraffers.chat.model.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByUser1IdAndUser2IdOrUser1IdAndUser2Id(Long user1Id, Long user2Id, Long user2Id2, Long user1Id2);

}
