package com.ohgiraffers.dateapp.model.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "chat_rooms")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user1;

    @ManyToOne
    private User user2;

    public ChatRoom() {
    }

    public ChatRoom(Long id, User user1, User user2) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id=" + id +
                ", user1=" + user1 +
                ", user2=" + user2 +
                '}';
    }
}
