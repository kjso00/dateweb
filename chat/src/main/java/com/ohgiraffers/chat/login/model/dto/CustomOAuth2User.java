package com.ohgiraffers.chat.login.model.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    private final OAuth2Response oAuth2Response;
    private final String role;


    public CustomOAuth2User(OAuth2Response oAuth2Response, String role) {
        this.oAuth2Response = oAuth2Response;
        this.role = role;
    }

    @Override
    public Map<String, Object> getAttributes() { // 리소스 서버로부터 넘어오는 데이터
        return null;
    } // 리소스 서버에서 넘어오는 데이터

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // role 값

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role;
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return oAuth2Response.getName();
    }

    // 제공자 + 제공자에서 제공한 id로 사용자id 값을 임의로 설정함
    public String getUserId() {
        return oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
    }

    public OAuth2Response getOAuth(){
        return this.oAuth2Response;
    }
}