package com.ohgiraffers.chatjjj.controller;

import com.ohgiraffers.chatjjj.model.dto.MessageDTO;
import com.ohgiraffers.chatjjj.model.entity.Message;
import com.ohgiraffers.chatjjj.model.entity.User;
import com.ohgiraffers.chatjjj.service.ChatService;
import com.ohgiraffers.chatjjj.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ChatController {
    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/chat")
    public String chatPage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("currentUser", currentUser);

        // 모든 사용자 목록을 가져와 모델에 추가
        List<User> users = userService.findAllUsers(); // 모든 사용자 가져오기
        model.addAttribute("users", users);
        return "chat";
    }

    @GetMapping("/chat/{recipientUsername}")
    public String chatWithUser(@PathVariable String recipientUsername, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        User recipient = userService.findUserByUsername(recipientUsername);

        // 사용자가 로그인하지 않았더라도 recipient 정보가 존재하는지 확인
        if (recipient == null) {
            return "redirect:/login"; // 상대방 사용자 정보가 없으면 로그인 페이지로 리다이렉트
        }

        // 모델에 사용자 정보 추가
        model.addAttribute("otherUser", recipient);
        model.addAttribute("messages", chatService.getChatMessages(currentUser, recipient));
        model.addAttribute("currentUser", currentUser); // 현재 사용자 정보를 추가하여 메시지를 보낼 때 사용

        return "chatroom"; // 채팅방 페이지로 이동
    }

    @MessageMapping("/send/message")
    public void sendMessageWithDTO(@Payload MessageDTO messageDTO) {
        User senderUser = userService.findById(messageDTO.getSenderId());
        User recipientUser = userService.findById(messageDTO.getRecipientId());
        if (recipientUser != null) {
            Message message = new Message();
            message.setSender(senderUser);
            message.setRecipient(recipientUser);
            message.setContent(messageDTO.getContent());

            // 메시지를 데이터베이스에 저장
            chatService.saveMessage(message);

            // 메시지를 전송
            messagingTemplate.convertAndSendToUser(
                    recipientUser.getUsername(), "/queue/messages", message);
        }
    }

}
