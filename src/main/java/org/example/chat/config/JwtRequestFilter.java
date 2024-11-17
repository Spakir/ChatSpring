package org.example.chat.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.chat.model.User;
import org.example.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Value("${spring.datasource.jwtKey}")
    private String SECRET_KEY;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String path = request.getRequestURI();
        System.out.println("authorizationHeader:" + authorizationHeader);

        if (path.equals("/login.html") || path.equals("/login")) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
        } else {
            String cookieToken = getTokenFromCookies(request.getCookies());
            if (cookieToken != null) {
                jwt = cookieToken.replace("Bearer ", "");
            }
        }
        System.out.println("JWT:" + jwt);
        if (jwt != null) {
            try {
                Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody();
                Long userId = Long.valueOf(claims.getSubject()); // Извлекаем ID пользователя

                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = userService.loadUserById(userId);
                    if (user != null) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(user, null, null);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    } else {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User  not found");
                        return;
                    }
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private String getTokenFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("username",user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 8640000))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

}
