package org.example.chat.config;

import lombok.RequiredArgsConstructor;
import org.example.chat.mapper.UserMapper;
import org.example.chat.repository.UserRepository;
import org.example.chat.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class UserManagementConfig {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Bean
    UserDetailsService userDetailsService(){
        return new CustomUserDetailsService(userRepository,userMapper);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
