package com.ohgiraffers.chat.login.model.entity;

public enum Role {
    USER("USER"),
    GUEST("GUEST"),
    ADMIN("ADMIN");
    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getKey(){ return role; }

    @Override
    public String toString() {
        return "Role{" +
                "role='" + role + '\'' +
                '}';
    }
}
