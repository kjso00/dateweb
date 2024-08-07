package com.ohgiraffers.chattest.model.entity;


import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    private String content;

    private LocalDateTime timestamp;

    // 기본 생성자
    public ChatMessage() {}

    // 모든 필드를 포함한 생성자
    public ChatMessage(ChatRoom chatRoom, User sender, User recipient, String content) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    // Getter 메소드
    public Long getId() { return id; }
    public ChatRoom getChatRoom() { return chatRoom; }
    public User getSender() { return sender; }
    public User getRecipient() { return recipient; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // Setter 메소드
    public void setId(Long id) { this.id = id; }
    public void setChatRoom(ChatRoom chatRoom) { this.chatRoom = chatRoom; }
    public void setSender(User sender) { this.sender = sender; }
    public void setRecipient(User recipient) { this.recipient = recipient; }
    public void setContent(String content) { this.content = content; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", chatRoom=" + chatRoom.getId() +
                ", sender=" + sender.getId() +
                ", recipient=" + recipient.getId() +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
