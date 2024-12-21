package org.example.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.chat.dto.UserDto;
import org.example.chat.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api")
@Controller
@RequiredArgsConstructor
public class UserHtmlController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "/login"; // Возвращаем имя HTML-шаблона
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "success";
    }

    @GetMapping("/register")
    public String getRegisterShow(@ModelAttribute("user") UserDto user) {
        return "create";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") UserDto user) {
        userService.createUser(user);
        return "redirect:/api/login";
    }

    @GetMapping("/chat")
    public String showChatPage() {
        return "/chat"; // Возвращаем имя HTML-шаблона для страницы чата
    }
}
