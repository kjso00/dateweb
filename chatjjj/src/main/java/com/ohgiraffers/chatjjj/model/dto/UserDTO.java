package com.ohgiraffers.chatjjj.model.dto;

import com.ohgiraffers.chatjjj.model.entity.User;

public class UserDTO {

    private Long id;
    private String username;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

    public UserDTO() {
    }

    public UserDTO(Long id, String username) {
        this.id = id;
        this.username = username;
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

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
