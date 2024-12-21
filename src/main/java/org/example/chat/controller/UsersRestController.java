package org.example.chat.controller;

import org.example.chat.dto.UserDto;
import org.example.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UsersRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(){
        UserDto userDto = userService.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(userDto);
    }
}
