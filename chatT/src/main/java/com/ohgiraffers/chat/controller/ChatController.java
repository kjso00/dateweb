package com.ohgiraffers.chat.controller;


import com.ohgiraffers.chat.model.dto.ChatMessageDTO;
import com.ohgiraffers.chat.model.entity.ChatMessage;
import com.ohgiraffers.chat.model.entity.ChatRoom;
import com.ohgiraffers.chat.model.entity.User;
import com.ohgiraffers.chat.repository.ChatUserRepository;
import com.ohgiraffers.chat.service.ChatService;
import com.ohgiraffers.chat.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
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

    @Autowired
    private ChatUserRepository userRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    // SimpMessagingTemplate: 스프링의 STOMP메시징 지원을 위한 핵심 클래스
    // 서버에서 클라이언트로 메시지를 쉽게 보낼 수 있게 해주는 유틸리티 클래스
    // 이 설정을 통해 ChatController는 웹소켓을 통해 실시간으로 메시지를 보낼 수 있는 능력을 갖게됨
    @Autowired
    private final SimpMessagingTemplate messagingTemplate; // 메시징 템플릿

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }



    @GetMapping("/")
    public String index(){
        return "index";
    }


    // 채팅방페이지 로드할 때 필요한 모든 정보를 준비하고, 이를 뷰에 전달하는 역할
    @GetMapping("/chat/room/{roomId}")              // HttpSession session: 현재 사용자의 세션 정보를 얻기 위해 사용
    public String chatRoom(@PathVariable Long roomId, Model model, HttpSession session) {
        // session.getAttribute("user"): 현재 HTTP세션에서 "user"라는 이름으로 저장된 속성을 가져옴
        // 반환된 객체를 User타입으로 캐스팅
        // 왜 User를 ()로 감싸냐. 타입캐스팅을 하기 위해서인데 세션은 다양한 타입의 객체를 저장할 수 있기 때문에, 기본적으로 Object로 반환됨
        // 이래서 Object타입의 반환값을 User타입으로 변환(캐스팅)하라고 컴파일러에게 지시하는거임.
        // 즉, 이 객체는 실제로 User타입이니, User타입으로 취급해도 좋다고 명시적으로 알려주는거다
        User currentUser = (User) session.getAttribute("user");
        // 세션에서 가져온 사용자 정보가 null인지 확인 null이면 로그인페이지로 이동
        if (currentUser == null) {
            return "redirect:/login";
        }
        // 채팅방 정보 조회. 해당ID의 채팅방 정보를 가져옴
        ChatRoom chatRoom = chatService.getChatRoomById(roomId);
        User otherUser = chatRoom.getUser1().getId().equals(currentUser.getId())
                ? chatRoom.getUser2()
                : chatRoom.getUser1();

        model.addAttribute("chatRoomId", roomId);
        model.addAttribute("messages", chatService.getChatMessages(roomId));
        model.addAttribute("otherUser", otherUser);
        model.addAttribute("currentUser", currentUser);
        return "chatroom";
    }


    // @MessageMapping: 웹소켓을 통해 실시간으로 메시지를 주고받을 때 사용
    // ex) 1.채팅 메시지 송수신 2.실시간 알림 전송 3.실시간 데이터 업데이트
    @MessageMapping("/chat/{roomId}")
    // @DestinationVariable Long roomId: WebSocket 메시지 경로에서 추출한 채팅방 ID
    // ChatMessageDTO messageDTO: 클라이언트가 전송한 채팅 메시지 데이터
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageDTO messageDTO) {
        // 발신자ID와 수신자ID가 null인지 확인하고 null이면 예외를 발생시킴
        if (messageDTO.getSenderId() == null || messageDTO.getRecipientId() == null) {
            throw new IllegalArgumentException("Sender ID or Recipient ID cannot be null");
        }
        // 채팅방 확인. 채팅방이 존재하지 않으면 예외를 발생시킴
        ChatRoom chatRoom = chatService.findChatRoomById(roomId);
        if (chatRoom == null) {
            throw new IllegalArgumentException("Chat room not found");
        }
        // 발신자와 수신자의 정보를 데이터베이스에서 조회
        User sender = chatService.findUserById(messageDTO.getSenderId());
        User recipient = chatService.findUserById(messageDTO.getRecipientId());
        // 채팅 메시지 객체 생성- 메시지가 속한 채팅방객체, 메시지를 보낸 사용자 객체, 메시지를 받는 사용자 객체, 실제 메시지 내용
        ChatMessage chatMessage = new ChatMessage(chatRoom,sender, recipient, messageDTO.getContent());
        chatMessage.setTimestamp(LocalDateTime.now());
        // 메시지를 데이터베이스에 저장
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
//        users.remove(currentUser); // 현재 사용자를 목록에서 제외
        users.removeIf(user -> user.getId().equals(currentUser.getId())); // ID로 비교하여 제거
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

        ChatRoom chatRoom = chatService.findOrCreateChatRoom(currentUser.getId(), userId);
        return "redirect:/chat/room/" + chatRoom.getId();
    }



}
