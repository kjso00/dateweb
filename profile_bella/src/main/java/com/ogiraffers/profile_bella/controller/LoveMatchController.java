package com.ogiraffers.profile_bella.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lovematch/")
public class LoveMatchController {

    @GetMapping("match")
    public String match() {
        return "loveMatch/match";
    }
}
