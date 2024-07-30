1. @MessageMapping 사용해서 웹소켓을 통해 송수신 하는 메소드 만들기
2. 클라이언트가 특정 주제 구독해서 특정 경로에서 발생하는 메시지를 수신
3. EX) 구독 
서버 측
4. @SendTo("/topic/general")
클라이언트 측
5. stompClient.subscribe('/topic/general')

6. @SendToUser로 특정 사용자에게 메시지를 보냄
7. 일대일 채팅은 구독이 필요없음
8. 특정 사용자 간의 연결을 통해 메시지 전달
