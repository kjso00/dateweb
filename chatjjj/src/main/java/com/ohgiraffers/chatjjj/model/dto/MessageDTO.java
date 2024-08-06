package com.ohgiraffers.chatjjj.model.dto;

public class MessageDTO {

    private Long senderId;      // 보낸 사람의 ID
    private Long recipientId;   // 받는 사람의 ID
    private String content;     // 메시지 내용

    public MessageDTO() {
    }

    public MessageDTO(Long senderId, Long recipientId, String content) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "senderId=" + senderId +
                ", recipientId=" + recipientId +
                ", content='" + content + '\'' +
                '}';
    }
}
