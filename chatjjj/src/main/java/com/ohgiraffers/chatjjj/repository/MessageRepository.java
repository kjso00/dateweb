package com.ohgiraffers.chatjjj.repository;

import com.ohgiraffers.chatjjj.model.entity.Message;
import com.ohgiraffers.chatjjj.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndRecipientOrRecipientAndSenderOrderByIdAsc(User sender1, User recipient1, User sender2, User recipient2);
}
