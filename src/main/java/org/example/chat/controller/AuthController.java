//package org.example.chat.controller;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.example.chat.model.User;
//import org.example.chat.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController // Изменяем на RestController для работы с JSON
//@RequestMapping("/api")
//public class AuthController {
//
//    @Autowired
//    private UserService userService;
//
//    @Value("${spring.datasource.jwtKey}")
//    private String SECRET_KEY;
//
//    @PostMapping("/login")
//    public ResponseEntity<Map<String, Object>> login(@RequestBody User userCredentials) {
//        Map<String, Object> response = new HashMap<>();
//        try {
//            User authenticatedUser  = userService.authenticate(userCredentials.getUsername(), userCredentials.getPassword());
//            if (authenticatedUser  != null) {
//                String token = generateToken(authenticatedUser );
//                response.put("token", token);
//                response.put("username", authenticatedUser .getUsername());
//                System.out.println("Сгенерированный токен: " + token);
//
//                return ResponseEntity.ok(response); // Возвращаем 200 OK с токеном
//            } else {
//                response.put("error", "Неверное имя пользователя или пароль");
//                return ResponseEntity.status(401).body(response); // Возвращаем 401 Unauthorized
//            }
//        } catch (Exception e) {
//            response.put("error", "Ошибка сервера: " + e.getMessage());
//            return ResponseEntity.status(500).body(response); // Возвращаем 500 Internal Server Error
//        }
//    }
//
//    private String generateToken(User user) {
//        return Jwts.builder()
//                .setSubject(user.getUsername())
//                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Токен действителен 1 день
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }
//
//}
//
