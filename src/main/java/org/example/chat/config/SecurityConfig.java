package org.example.chat.config;

import lombok.RequiredArgsConstructor;
import org.example.chat.provider.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.authenticationProvider(authenticationProvider);
        http
                .csrf(csrf -> csrf.disable()) // Отключение CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login", "/login", "/favicon.ico", "/api/register").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
