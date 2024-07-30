package com.ohgiraffers.dateapp.model.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ChatRoom chatRoom;

    @ManyToOne
    private User sender;

    private String content;

    private Long roomId;

    private Long recipientId; // 수신자 ID 추가

    private LocalDateTime timestamp;

    public ChatMessage() {
    }

    public ChatMessage(Long id, ChatRoom chatRoom, User sender, String content, Long roomId, Long recipientId, LocalDateTime timestamp) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = content;
        this.roomId = roomId;
        this.recipientId = recipientId;
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

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
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
                ", recipientId=" + recipientId +
                ", timestamp=" + timestamp +
                '}';
    }
}
