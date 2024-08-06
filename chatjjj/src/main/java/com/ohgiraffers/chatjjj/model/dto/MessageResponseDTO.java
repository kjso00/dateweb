package com.ohgiraffers.chatjjj.model.dto;

import com.ohgiraffers.chatjjj.model.entity.Message;

public class MessageResponseDTO {

    private Long id;
    private UserDTO sender;
    private UserDTO recipient;
    private String content;

    public MessageResponseDTO(Message message) {
        this.id = message.getId();
        this.sender = new UserDTO(message.getSender());
        this.recipient = new UserDTO(message.getRecipient());
        this.content = message.getContent();
    }

    public MessageResponseDTO() {
    }

    public MessageResponseDTO(Long id, UserDTO sender, UserDTO recipient, String content) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getSender() {
        return sender;
    }

    public void setSender(UserDTO sender) {
        this.sender = sender;
    }

    public UserDTO getRecipient() {
        return recipient;
    }

    public void setRecipient(UserDTO recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MessageResponseDTO{" +
                "id=" + id +
                ", sender=" + sender +
                ", recipient=" + recipient +
                ", content='" + content + '\'' +
                '}';
    }
}
