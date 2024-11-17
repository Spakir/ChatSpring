package org.example.chat.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.chat.model.User;
import org.example.chat.service.MessageService;
import org.example.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class UsersController {

    @Autowired
    private UserService userService;

    @Value("${spring.datasource.jwtKey}")
    private String SECRET_KEY;

    @GetMapping("/login")
    public String showLoginPage() {
        return "/login"; // Возвращаем имя HTML-шаблона
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginingUser ) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            User authenticatedUser  = userService.authenticate(loginingUser .getUsername(), loginingUser .getPassword());
            if (authenticatedUser  != null) {
                String token = generateToken(authenticatedUser ); // Генерация токена
                responseMap.put("token", token);
                responseMap.put("username", authenticatedUser .getUsername());

                System.out.println("Сгенерированный токен: " + token);
                return ResponseEntity.ok(responseMap); // Возвращаем JSON ответ
            } else {
                responseMap.put("error", "Неверное имя пользователя или пароль");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap); // Возвращаем JSON ответ с ошибкой
            }
        } catch (Exception e) {
            System.out.println("Ошибка при аутентификации: " + e.getMessage());
            responseMap.put("error", "Ошибка сервера: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap); // Возвращаем JSON ответ с ошибкой
        }
    }

    @GetMapping("/success")
    public String showSuccessPage(Model model, @RequestParam(required = false) String token) {
        System.out.println("Метод showSuccessPage вызван."); // Логируем вызов метода
        System.out.println("Полученный токен: " + token); // Логируем полученный токен
        if (token != null) {
            System.out.println("Полученный токен: " + token); // Логируем полученный токен
            if (validateToken(token)) {
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token)
                        .getBody();
                Long userId = Long.valueOf(claims.getSubject());
                User user = userService.loadUserById(userId);
                model.addAttribute("username", user.getUsername());
                return "/success"; // Возвращаем имя HTML-шаблона для успешной авторизации
            } else {
                System.out.println("Токен недействителен или истек."); // Логируем недействительность токена
            }
        } else {
            System.out.println("Токен не предоставлен."); // Логируем отсутствие токена
        }
        return "redirect:/login"; // Перенаправляем на страницу авторизации, если токен недействителен
    }

    @GetMapping("/chat")
    public String showChatPage() {
        return "/chat"; // Возвращаем имя HTML-шаблона для страницы чата
    }

    @GetMapping("/register")
    public String getRegisterShow(@ModelAttribute("user") User user) {
        return "/public/create";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user) {
        userService.createUser (user);
        return "redirect:/api/login";
    }

    private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId())) // Используем ID пользователя
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            Long userId = Long.valueOf(claims.getSubject()); // Извлекаем ID пользователя
            Date expirationDate = claims.getExpiration();
            return expirationDate != null && !expirationDate.before(new Date()) && userId != null;
        } catch (Exception e) {
            System.out.println("Ошибка валидации токена: " + e.getMessage()); // Логируем ошибку валидации
            return false;
        }
    }
}
