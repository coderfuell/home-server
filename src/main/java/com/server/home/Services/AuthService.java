package com.server.home.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.server.home.Model.User;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    public void addAuthToken(HttpServletResponse response, User user) {
        Cookie cookie = jwtService.generateAccessCookie(user);
        response.addCookie(cookie);
    }

    public void addRefreshToken(HttpServletResponse response, User user) {
        Cookie cookie = jwtService.generateRefreshCookie(user);
        response.addCookie(cookie);
    }

    private void addRefreshToken(HttpServletResponse response, String username) {
        User user = userDetailsService.loadUserByUsername(username);
        addRefreshToken(response, user);
    }

    private String authenticateRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String username = jwtService.validateRefreshToken(cookies);
        return username;
    }

    public void authenticate(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String username;
        try {
            username = jwtService.getUsername(cookies);
        } catch (ExpiredJwtException e) {
            username = authenticateRefreshToken(request);
            addRefreshToken(response, username);
        }
        User user = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);
    }

}
