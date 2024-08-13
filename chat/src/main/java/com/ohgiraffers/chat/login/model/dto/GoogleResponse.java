package com.ohgiraffers.chat.login.model.dto;

import java.util.Map;

public class GoogleResponse implements OAuth2Response{
    private final Map<String, Object> attribute;
    private Long userNum;

    public GoogleResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public void setUserNum(Long id) {
        userNum = id;
    }

    @Override
    public Long getUserNum() {
        return userNum;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}