package com.ohgiraffers.chat.login.model.dto;

import java.util.Map;

public class NaverResponse implements OAuth2Response{
    private final Map<String, Object> attribute;
    private Long userNum;

    public NaverResponse(Map<String, Object> attribute) {
        // attribute.get("response")의 반환값은 Object이기 때문에 타입캐스팅
        this.attribute = (Map<String, Object>) attribute.get("response");
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
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
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

