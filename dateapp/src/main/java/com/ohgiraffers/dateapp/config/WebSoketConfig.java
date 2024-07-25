package com.ohgiraffers.dateapp.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// 웹소켓 설정을 쉽게 할 수 있도록 도와주는 어노테이션
// STOMP를 통해 서버는 특정 채팅방에 가입한 사용자만 메시지를 수신하도록 설정
// 채팅 기능을 구현하기 위해 STOMP 기반 설정과 직접 WebSocket 핸들러 설정을 하는 방법 있는데
// 지금 하고있는 프로젝트는 사용자가 다른 사용자에게 채팅 요처을 보내고, 상대방이 요청을 수락하면
// 채팅방이 생성되어 채팅이 가능하게 하도록 해야되기 때문에 STOMP가 적합
// Queue방식으로 웹소켓 설정

// 설정 클래스를 정의할 때 사용하는 어노테이션
@Configuration
// 웹소켓 메시지 처리를 활성화 하는 데 사용
@EnableWebSocketMessageBroker
public class WebSoketConfig implements WebSocketMessageBrokerConfigurer {


    // sockJS Fallback을 이용해 노출할 endpoint 설정
    // sockJS Fallback: 웹소켓 연결이 불가능한 환경에서 실시간 양방향 통신을 가능하게 하는 대체 기술
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { // STOMP 엔드포인트를 등록하는 메소드
        // 웹소켓이 handshake를 하기 위해 연결하는 endpoint
        // 엔드포인트: 채팅기능을 제공하는 URL 이라고 생각하면됨
        registry.addEndpoint("/ws")  // "/ws" 경로로 WebSocket 엔드포인트를 추가, 클라이언트는 이 경로로 웹소켓 연결을 시도
                .setAllowedOriginPatterns("*") // 모든 오리진(출처)에서의 연결을 허용 (CORS 설정)
                                                // CORS: 서버가 웹 브라우저의 요청에 대해 다른 출처에서의 접근을 허용할 수 있도록 하는 표준
                .withSockJS(); // SockJS 폴백 옵션을 활성화

    }

    //메세지 브로커에 관한 설정
    // /sub로 시작하는 주제를 구독하고, /pub으로 시작하는 목적지로 메시지를 보냄
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {  // 메시지 브로커 구성을 위한 메소드
        // 서버 -> 클라이언트로 메시지를 발행하기 위한 주제(topic) 접두사 설정
//        registry.enableSimpleBroker("/sub");  // 서버 -> 클라이언트로 발행하는 메세지에 대한 endpoint 설정 : 구독 , /sub 접두사로 시작하는 주제를 구독하는 클라이언트에게 메시지를 브로드캐스트
        registry.enableSimpleBroker("/topic", "/queue", "/user");
        // 클라이언트 -> 서버로 메시지를 발행하기 위한 주제 접두사 설정                                            // ex) 사용자가 /sub/chat/을 구독하면, 서버는 이 주제로 메시지를 발행
        registry.setApplicationDestinationPrefixes("/pub");  // 클라이언트->서버로 발행하는 메세지에 대한 endpoint 설정 : 구독에 대한 메세지
    }
}

