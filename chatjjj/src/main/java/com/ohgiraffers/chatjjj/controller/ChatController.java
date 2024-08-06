package com.ohgiraffers.chatjjj.controller;

import com.ohgiraffers.chatjjj.model.dto.MessageDTO;
import com.ohgiraffers.chatjjj.model.dto.MessageResponseDTO;
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

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
        if (currentUser == null) {
            return "redirect:/login";
        }

        Optional<User> recipientOptional = userService.findByUsername(recipientUsername);
        if (recipientOptional.isEmpty()) {
            model.addAttribute("error", "User not found");
            return "chat";
        }

        User recipient = recipientOptional.get();
        model.addAttribute("otherUser", recipient);
        model.addAttribute("messages", chatService.getChatMessages(currentUser, recipient));
        model.addAttribute("currentUser", currentUser);

        return "chatroom";
    }

    @MessageMapping("/send/message")
    public void sendMessageWithDTO(@Payload MessageDTO messageDTO, Principal principal) {
        try {
            Optional<User> senderOptional = userService.findByUsername(principal.getName());
            Optional<User> recipientOptional = userService.findById(messageDTO.getRecipientId());

            if (senderOptional.isEmpty() || recipientOptional.isEmpty()) {
                throw new RuntimeException("Sender or Recipient not found");
            }

            User senderUser = senderOptional.get();
            User recipientUser = recipientOptional.get();

            Message message = new Message();
            message.setSender(senderUser);
            message.setRecipient(recipientUser);
            message.setContent(messageDTO.getContent());

            chatService.saveMessage(message);

            MessageResponseDTO responseDTO = new MessageResponseDTO(message);
            messagingTemplate.convertAndSendToUser(
                    recipientUser.getUsername(), "/queue/messages", responseDTO);
        } catch (Exception e) {
            // 에러 로깅 및 클라이언트에 에러 메시지 전송
            messagingTemplate.convertAndSendToUser(
                    principal.getName(), "/queue/errors", "Message sending failed: " + e.getMessage());
        }
    }
}
