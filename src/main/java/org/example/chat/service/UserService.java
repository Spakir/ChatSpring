package org.example.chat.service;

import lombok.AllArgsConstructor;
import org.example.chat.dto.UserDto;
import org.example.chat.mapper.UserMapper;
import org.example.chat.model.User;
import org.example.chat.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser (UserDto user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(userMapper.toUser(user));
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow();
    }

    public UserDto findByName(String username){
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow();
    }
}
