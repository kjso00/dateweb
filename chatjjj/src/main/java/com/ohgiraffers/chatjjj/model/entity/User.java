package com.ohgiraffers.chatjjj.model.entity;


import jakarta.persistence.*;


import java.util.Set;

@Entity
@Table(name = "chat_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;


    private String password;


    @OneToMany(mappedBy = "sender")
    private Set<Message> sentMessages;


    @OneToMany(mappedBy = "recipient")
    private Set<Message> receivedMessages;

    public User() {
    }

    public User(Long id, String username, String password, Set<Message> sentMessages, Set<Message> receivedMessages) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.sentMessages = sentMessages;
        this.receivedMessages = receivedMessages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(Set<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public Set<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(Set<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sentMessages=" + sentMessages +
                ", receivedMessages=" + receivedMessages +
                '}';
    }
}

