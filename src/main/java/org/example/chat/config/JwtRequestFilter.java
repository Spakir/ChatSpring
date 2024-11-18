package org.example.chat.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.example.chat.model.User;
import org.example.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Value("${spring.datasource.jwtKey}")
    private String SECRET_KEY;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String path = request.getRequestURI();
        System.out.println(response);
        System.out.println("authorizationHeader:" + authorizationHeader);

        if (path.equals("/login.html") || path.equals("/api/login")) {
            System.out.println("хуй");
            chain.doFilter(request, response);
            return;
        }

        String jwt = null;

        // Проверка заголовка Authorization
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
        } else {
            // Проверка токена в куках
            String cookieToken = getTokenFromCookies(request.getCookies());
            if (cookieToken != null) {
                jwt = cookieToken.replace("Bearer ", "");
            }
        }

        // Проверка токена в параметрах URL
        if (jwt == null) {
            String tokenFromUrl = request.getParameter("token");
            if (tokenFromUrl != null) {
                jwt = tokenFromUrl;
            }
        }

        if (jwt != null) {
            try {
                Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody();
                Long userId = Long.valueOf(claims.getSubject()); // Извлекаем ID пользователя
                String username = claims.get("username",String.class);

                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null && username != null) {
                    User user = userService.loadUserById(userId);
                    if (user != null) {
                        Authentication exitingToken = SecurityContextHolder.getContext().getAuthentication();
                        if(exitingToken == null){
                            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user,null,null));
                        }
                        System.out.println(SecurityContextHolder.getContext().getAuthentication());
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
