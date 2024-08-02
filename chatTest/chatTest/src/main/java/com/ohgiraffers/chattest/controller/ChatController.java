package com.ohgiraffers.chattest.controller;


import com.ohgiraffers.chattest.model.entity.ChatMessage;
import com.ohgiraffers.chattest.model.entity.ChatRoom;
import com.ohgiraffers.chattest.model.entity.User;
import com.ohgiraffers.chattest.repository.UserRepository;
import com.ohgiraffers.chattest.service.ChatService;
import com.ohgiraffers.chattest.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ChatController {

    // @MessageMapping 웹소켓을 통해 실시간으로 메시지를 주고받을 때 사용
    // 1. 채팅 메시지 송수신
    // 2. 실시간 알림 전송
    // 3. 실시간 데이터 업데이트

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;


   @Autowired
    private final SimpMessagingTemplate messagingTemplate; // 메시징 템플릿

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }



    // 메인화면
    @GetMapping("/")
    public String index() {
        return "index";

    }

    // 채팅요청 페이지 렌더링
    // 세선에 저장된 사용자 정보와 url경로의 사용자 이름을 비교
//    @GetMapping("/chat/{username}")
//    public String chatPage(@PathVariable String username, Model model, HttpSession session) {  // HttpSession: 현재 사용자 세션을 나타냄
//        // 세션에서 "user"라는 키로 저장된 사용자 객체를 가져옴
//        User user = (User) session.getAttribute("user");
//        if (user == null || !user.getUsername().equals(username)) {
//            return "redirect:/login";
//        }
//        model.addAttribute("username", username);
//        return "chat";
//    }


    // startChat 메소드에서 리다이렉트된 후 호출
    // 채팅방 정보, 메시지 목록, 상대방 정보 등을 모델에 추가해서 채팅 페이지 렌더링
    @GetMapping("/chat/room/{roomId}")
    public String chatRoom(@PathVariable Long roomId, Model model, HttpSession session) {
        // 현재 로그인한 사용자 정보를 세션에서 가져옴
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        // 채팅방id 조회하고 chatRoom 변수에 저장
        ChatRoom chatRoom = chatService.getChatRoomById(roomId);
        // chatRoom.getUser1().equals(currentUser): 채팅방의 user1이 현재 사용자인지 확인
        // 만약 user1이 현재 사용자라면, chatRoom.getUser2()를 반환하여 상대방을 user2로 설정
        // 이렇게 결정된 상대방 사용자 객체를 otherUser에 저장
        User otherUser = chatRoom.getUser1().equals(currentUser) ? chatRoom.getUser2() : chatRoom.getUser1();
        // 모델에 채팅방Id, 채팅 메시지 목록, 상대방 사용자의 이름을 추가
        model.addAttribute("chatRoomId", roomId);
        model.addAttribute("messages", chatService.getChatMessages(roomId));
        model.addAttribute("otherUser", otherUser.getUsername());
        return "chatroom";
    }

    // 메시지 전송
    @MessageMapping("/chat/{roomId}")
    // @DestinationVariable Long roomId: 사용자가 보낸 메시지가 어떤 채팅방과 관련이 있는지를 식별
    // ChatMessage message에는 채팅메시지 내용, 발신자ID, 수신자ID 등을 포함하고있어야됨
    public void sendMessage(@DestinationVariable Long roomId, ChatMessage message) {
        // 유효성 검사
        if (message.getSender() == null || message.getRecipient() == null) {
            throw new IllegalArgumentException("Sender ID or Recipient ID cannot be null");
        }
        // ChatRoom 객체 가져오기 (예시: roomId로부터 ChatRoom 객체를 조회)
        ChatRoom chatRoom = chatService.findChatRoomById(roomId);
        if (chatRoom == null) {
            throw new IllegalArgumentException("Chat room not found");
        }

        // 예시: 현재 로그인한 사용자 정보를 가져오는 방법
        User user = userService.findById(message.getSender()); // 발신자 정보
        User recipientUser = userService.findById(message.getRecipient()); // 수신자 정보

        // ChatMessage 객체 생성 시 sender 및 recipient 설정
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(user); // user는 발신자 User 객체
        chatMessage.setRecipient(recipientUser); // recipientUser는 수신자 User 객체
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setContent(message.getContent());

        // 메시지 저장
        chatService.saveMessage(chatMessage);
        // 메시지 처리 로직 (예: 데이터베이스에 저장)
//        chatService.saveMessage(roomId, message.getSenderId(), message.getContent(), message.getRecipientId());
        // 상대방에게 메시지 전송
        messagingTemplate.convertAndSendToUser(    // convertAndSendToUser: 특정 사용자에게 메시지를 전송하는 메서드
                message.getRecipient().toString(),  // 메시지를 받을 사용자의 ID를 문자열로 변환하여 지정
                "/queue/messages",    // 사용자가 수신할 메시지의 대기열
                message    // 실제로 전송할 메시지 객체
        );
    }

    // 채팅방 나가기
    @PostMapping("/chat/leave")
    @ResponseBody
    public Map<String, Object> leaveChatRoom(@RequestParam Long chatRoomId) {
        chatService.deleteChatRoom(chatRoomId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return response;
    }

    // 사용자 목록 조회
    @GetMapping("/users")
    public String userList(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        List<User> users = userRepository.findAll();
        users.remove(currentUser); // 현재 사용자를 목록에서 제외
        model.addAttribute("users", users);
        return "userList";
    }

    @GetMapping("/chat/messages/{chatRoomId}")
    @ResponseBody
    public List<ChatMessage> getChatMessages(@PathVariable Long chatRoomId) {
        return chatService.getChatMessages(chatRoomId);
    }

    // 수정코드

    // 사용자가 다른 사용자와 채팅을 시작하려고 할 때 호출
    @GetMapping("/chat/start/{userId}")
    public String startChat(@PathVariable Long userId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        // 채팅방이 이미 존재하는지 확인
        ChatRoom existingRoom = chatService.findChatRoomByUsers(currentUser.getId(), userId);

        if (existingRoom != null) {
            // 기존 채팅방이 있으면 해당 방으로 리다이렉트
            return "redirect:/chat/room/" + existingRoom.getId();
        } else {
            // 새 채팅방 생성
            ChatRoom newRoom = chatService.createChatRoom(currentUser.getId(), userId);
            return "redirect:/chat/room/" + newRoom.getId();
        }
    }



}