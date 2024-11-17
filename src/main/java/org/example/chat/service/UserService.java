package org.example.chat.service;

import lombok.AllArgsConstructor;
import org.example.chat.model.User;
import org.example.chat.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {
    public final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser (User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordMatches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    private boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User loadUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
