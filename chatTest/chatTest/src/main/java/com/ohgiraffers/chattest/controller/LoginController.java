package com.ohgiraffers.chattest.controller;

import com.ohgiraffers.chattest.model.entity.User;
import com.ohgiraffers.chattest.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class LoginController {

    @Autowired

    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "/login";
    }

//    @PostMapping("/login")
//    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
//        User user = userService.loginUser(username, password);
//        if (user != null) {
//            session.setAttribute("user", user);
//            return "redirect:/chat/" + username;
//        }
//        return "redirect:/login?error";
//    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        User user = userService.loginUser(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/users"; // 사용자 목록 페이지로 리다이렉트
        }
        return "redirect:/login?error";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        userService.registerUser(username, password);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
