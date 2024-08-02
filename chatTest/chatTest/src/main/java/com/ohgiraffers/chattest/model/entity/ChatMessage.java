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
    private User sender; // User 객체 추가

    private String content;

    private Long roomId;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient; // User 객체 추가

    private LocalDateTime timestamp;

    public ChatMessage() {
    }

    public ChatMessage(Long id, ChatRoom chatRoom, User sender, String content, Long roomId, User recipient, LocalDateTime timestamp) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = content;
        this.roomId = roomId;
        this.recipient = recipient;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", chatRoom=" + chatRoom +
                ", sender=" + sender +
                ", content='" + content + '\'' +
                ", roomId=" + roomId +
                ", recipient=" + recipient +
                ", timestamp=" + timestamp +
                '}';
    }
}
