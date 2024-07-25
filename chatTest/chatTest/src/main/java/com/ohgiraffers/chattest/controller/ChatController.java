package com.ohgiraffers.chattest.controller;


import com.ohgiraffers.chattest.model.entity.ChatRoom;
import com.ohgiraffers.chattest.model.entity.User;
import com.ohgiraffers.chattest.service.ChatService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ChatController {
    @Autowired
    private ChatService chatService;

//    @GetMapping("/chat/{username}")
//    public String chatPage(@PathVariable String username, Model model) {
//        model.addAttribute("username", username);
//        return "chat";
//    }

    @GetMapping("/chat/{username}")
    public String chatPage(@PathVariable String username, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getUsername().equals(username)) {
            return "redirect:/login";
        }
        model.addAttribute("username", username);
        return "chat";
    }

    @PostMapping("/chat/request")
    @ResponseBody
    public Map<String, Object> sendChatRequest(@RequestParam String from, @RequestParam String to) {
        ChatRoom chatRoom = chatService.createChatRoom(from, to);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("chatRoomId", chatRoom.getId());
        return response;
    }

    @GetMapping("/chat/room/{chatRoomId}")
    public String chatRoom(@PathVariable Long chatRoomId, Model model) {
        model.addAttribute("chatRoomId", chatRoomId);
        model.addAttribute("messages", chatService.getChatMessages(chatRoomId));
        return "chatroom";
    }

    @PostMapping("/chat/send")
    @ResponseBody
    public void sendMessage(@RequestParam Long chatRoomId, @RequestParam String sender, @RequestParam String content) {
        chatService.saveMessage(chatRoomId, sender, content);
    }

    @PostMapping("/chat/leave")
    @ResponseBody
    public Map<String, Object> leaveChatRoom(@RequestParam Long chatRoomId) {
        chatService.deleteChatRoom(chatRoomId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return response;
    }


}