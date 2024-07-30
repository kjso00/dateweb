package com.ohgiraffers.chattest.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 메시지 브로커 구성 정의
    // 메시지가 큐에 저장된다는건 db에 저장된다는게 아니다
    // 메시지 큐:
    // 일시적 저장- 메시지가 수신자에게 전달되기 전까지 큐에 보관, 수신자가 수신하면 큐에서 제거나 특정조건이 만족되면 삭제
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // /queue 접두사로 시작하는 대상에 대해 메시지를 브로드캐스트
        // /queue: 일대일 채팅or개인 메시지 /queue/user123 로 메시지 보내면 특정 사용자 user123만 메시지를 받음
        config.enableSimpleBroker("/queue");
        // 클라이언트가 서버로 메시지 보낼 때 사용하는 주소 접두사를 "/app"으로 설정
        // 이 설정으로 클라이언트가 /app으로 시작하는 경로로 메시지를 전송할 때, 해당 메시지가 @@MessageMapping이 있는 메소드에 매핑
        // 클라이언트가 /app/hello로 메시지를 보내면 서버의 @MessageMapping("/hello") 메서드가 처리
        config.setApplicationDestinationPrefixes("/app");
    }

    // STOMP를 사용하여 클라이언트와 서버 간의 통신을 설정하는데 사용
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // "/ws"는 웹소켓 연결을 위한 url 경로
        // 클라이언트는 "/ws" 주소로 웹소켓 연결을 시작
        registry.addEndpoint("/ws").withSockJS();  // registry는 엔드포인트를 설정할 수 있는 객체
        // 클라이언트는 JS의 WebSocketAPI를 사용하여 /ws 경로로 연결을 시도
    }
}
