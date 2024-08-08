package com.ohgiraffers.chat.controller;


import com.ohgiraffers.chat.model.dto.ChatMessageDTO;
import com.ohgiraffers.chat.model.entity.ChatMessage;
import com.ohgiraffers.chat.model.entity.ChatRoom;
import com.ohgiraffers.chat.model.entity.User;
import com.ohgiraffers.chat.repository.UserRepository;
import com.ohgiraffers.chat.service.ChatService;
import com.ohgiraffers.chat.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // 특정 채팅방 페이지를 로드
    @GetMapping("/chat/room/{roomId}")
    public String chatRoom(@PathVariable Long roomId, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        ChatRoom chatRoom = chatService.getChatRoomById(roomId);
        User otherUser = chatRoom.getUser1().equals(currentUser) ? chatRoom.getUser2() : chatRoom.getUser1();

        model.addAttribute("chatRoomId", roomId);
        model.addAttribute("messages", chatService.getChatMessages(roomId));
        model.addAttribute("otherUser", otherUser);
        model.addAttribute("currentUsername", currentUser.getUsername());  // 추가
        return "chatroom";
    }


    // 2
    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageDTO messageDTO) {
        if (messageDTO.getSenderId() == null || messageDTO.getRecipientId() == null) {
            throw new IllegalArgumentException("Sender ID or Recipient ID cannot be null");
        }

        ChatRoom chatRoom = chatService.findChatRoomById(roomId);
        if (chatRoom == null) {
            throw new IllegalArgumentException("Chat room not found");
        }

        User sender = chatService.findUserById(messageDTO.getSenderId());
        User recipient = chatService.findUserById(messageDTO.getRecipientId());

        // 추가코드
        User actualRecipient = (sender.getId().equals(chatRoom.getUser1().getId()))
                ? chatRoom.getUser2()
                : chatRoom.getUser1();

        ChatMessage chatMessage = new ChatMessage(chatRoom, sender, actualRecipient, messageDTO.getContent());
        chatMessage.setTimestamp(LocalDateTime.now());

        chatService.saveMessage(chatMessage);

        // 채팅방의 모든 참여자에게 메시지 브로드캐스트
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, chatMessage);
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
    public List<ChatMessageDTO> getChatMessages(@PathVariable Long chatRoomId) {
        List<ChatMessage> messages = chatService.getChatMessages(chatRoomId);
        return messages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ChatMessageDTO convertToDTO(ChatMessage message) {
        return new ChatMessageDTO(
                message.getId(),
                message.getSender().getId(),
                message.getRecipient().getId(),
                message.getContent(),
                message.getTimestamp()
        );
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