package com.ohgiraffers.chat.login.service;

import com.ohgiraffers.chat.login.model.dto.CustomOAuth2User;
import com.ohgiraffers.chat.login.model.dto.GoogleResponse;
import com.ohgiraffers.chat.login.model.dto.NaverResponse;
import com.ohgiraffers.chat.login.model.dto.OAuth2Response;
import com.ohgiraffers.chat.login.model.entity.Role;
import com.ohgiraffers.chat.login.model.entity.UserEntity;
import com.ohgiraffers.chat.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사이트의 사용자 정보 데이터를 인자로 받아옴
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        // 네이버인지 구글인지 어떤 인증 provider인지
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes()); // dto 객체에 담음
        } else if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes()); // dto 객체에 담음
        } else {
            return null;
        }

        String userId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId(); // 임의로 만들어준 id
        UserEntity existData = userRepository.findByUserId(userId); // id값으로 repo에서 찾아서 넣어줌

        Role role; // enum 타입의 Role 클래스를 쓰기 위해 선언
        UserEntity userEntity = null;
        if (existData == null) { // 데이터가 없으면 신규회원
            userEntity = new UserEntity();
            userEntity.setUserId(userId);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setUserName(oAuth2Response.getName());
            role = Role.USER;

            // 임시로 관리자 권한 테스트
            if (oAuth2Response.getEmail().equals("zhuyaan93@gmail.com") || oAuth2Response.getEmail().equals("dansunmusik7@gmail.com")) {
                userEntity.setRole(Role.ADMIN);
            } else {
                userEntity.setRole(Role.USER);
            }
            userRepository.save(userEntity); // 제공자에게 받아온 정보 넣어주고 entity 저장
            oAuth2Response.setUserNum(userEntity.getId()); // user엔터티에 있는 id 번호를 UserNum에 그대로 넣어줌

        } else { // 존재하는경우 새로 업데이트 시켜줌
            existData.setUserId(userId);
            existData.setEmail(oAuth2Response.getEmail());
            existData.setUserName(oAuth2Response.getName());
            role = existData.getRole();
            existData.setRole(role);
            userRepository.save(existData); // 업데이트 한 정보 저장
            oAuth2Response.setUserNum(existData.getId());
        }

        return new CustomOAuth2User(oAuth2Response, role.getKey());
    }
}