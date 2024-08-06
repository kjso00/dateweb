package com.ohgiraffers.chatjjj.controller;


import com.ohgiraffers.chatjjj.model.entity.User;
import com.ohgiraffers.chatjjj.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // 회원가입 페이지로 이동
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String password, Model model) {
        if (userService.usernameExists(username)) {
            model.addAttribute("error", true);
            return "signup"; // 사용자 이름이 이미 존재하면 다시 회원가입 페이지로
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); // 실제 서비스에서는 비밀번호를 해시 처리해야 함
        userService.saveUser(newUser);
        return "redirect:/login"; // 회원가입 후 로그인 페이지로 리다이렉트
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        User user = userService.findUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            return "redirect:/chat";
        }
        return "redirect:/login?error";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
