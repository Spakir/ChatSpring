package org.example.chat.service;

import lombok.AllArgsConstructor;
import org.example.chat.dto.UserDto;
import org.example.chat.mapper.UserMapper;
import org.example.chat.model.UserDetailsImp;
import org.example.chat.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto user = userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow();

        UserDetails userDetails = new UserDetailsImp(user);

        return userDetails;
    }
}
