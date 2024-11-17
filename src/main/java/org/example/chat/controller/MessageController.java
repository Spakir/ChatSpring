package org.example.chat.controller;

import org.example.chat.model.Message;
import org.example.chat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/chat")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String,String> map){
        String message = map.get("message");
        String username = map.get("username");
        System.out.println("Получено сообщение от " + username + ": " + message);
        messageService.addMessage(username, message); // Измените здесь
        return ResponseEntity.ok().build();
    }
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> checkValidMessages(){
        List<Message> messageList = messageService.getMessages();
        System.out.println(messageList);
        return ResponseEntity.ok(messageList);
    }

}
