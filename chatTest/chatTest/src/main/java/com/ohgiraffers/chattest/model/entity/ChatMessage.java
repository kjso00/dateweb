package com.ohgiraffers.chattest.model.entity;


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

    private LocalDateTime timestamp;

    public ChatMessage() {
    }

    public ChatMessage(Long id, ChatRoom chatRoom, User sender, String content, LocalDateTime timestamp) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = content;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", chatRoom=" + chatRoom +
                ", sender=" + sender +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
