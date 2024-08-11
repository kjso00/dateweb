package com.ohgiraffers.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
// 웹소켓 메시지 브로커를 활성화
// 메시지 브로커: 발행자가 보낸 메시지를 구독자에게 전달해주는 역할
@EnableWebSocketMessageBroker  // WebSocketMessageBrokerConfigurer 인터페이스를 구현하여 웹소켓 관련 설정이 가능해짐
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 메시지 브로커 구성 정의
    // 메시지가 큐에 저장된다는건 db에 저장된다는게 아니다
    // 메시지 큐:
    // 일시적 저장- 메시지가 수신자에게 전달되기 전까지 큐에 보관, 수신자가 수신하면 큐에서 제거나 특정조건이 만족되면 삭제
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        // /queue와 topic 접두사로 시작하는 대상에 대해 메시지를 브로드캐스트
        // 브로드캐스트: 서버가 연결된 모든 클라이언트 또는 특정 그룹의 클라이언트에게 같은 메시지를 한 번에 보내는 것을 의미
        // topic: 일반적으로 "하나가 발행하고 여러 명이 구독"하는 방식 ex) 실시간 뉴스 업데이트, 주식 시세 정보 등
        // /queue: 일대일 채팅or개인 메시지 /queue/user123 로 메시지 보내면 특정 사용자 user123만 메시지를 받음
        config.enableSimpleBroker("/queue", "/topic");

        // 클라이언트가 서버로 메시지 보낼 때 사용하는 주소 접두사를 "/app"으로 설정
        // 이 설정으로 클라이언트가 /app으로 시작하는 경로로 메시지를 전송할 때, 해당 메시지가 @@MessageMapping이 있는 메소드에 매핑
        // 클라이언트가 /app/hello로 메시지를 보내면 서버의 @MessageMapping("/hello") 메서드가 처리
        config.setApplicationDestinationPrefixes("/app");
    }

    // STOMP를 사용하여 클라이언트와 서버 간의 통신을 설정하는데 사용
    // STOMP 엔드포인트 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // /ws는 클라이언트가 웹소켓 연결을 시작하는 URL경로
        // 실제 사용을 하려면 클아이언트에서 자바스크립트 코드로 연결해야됨
        // Socket은 웹소켓을 지원하지 않는 브라우저에도 실시간 통신을 가능하게 지원
        // =클라이언트는 JS의 WebSocketAPI를 사용하여 /ws 경로로 연결을 시도
        // ---------------------------------------------------------------
        // 연결 프로세스
        // 1. 클라이언트가 /ws 경로로 연결 요청을 보냄
        // 2. 서버는 이 요청을 받아 웹소켓 핸드셰이크를 수행함 (스프링부트에서는 핸드쎼이크 과정을 자동으로 처리해줌)
        // - 핸드셰이크: 웹소켓 연결을 시작할 때 클라이언트와 서버 간에 이루어지는 초기 통신 과정
        // 3. 연결이 성공하면, 클라이언트와 서버 간에 실시간 양방향 통신 채널이 열림
        registry.addEndpoint("/ws").withSockJS();  // registry는 엔드포인트를 설정할 수 있는 객체
    }

}
