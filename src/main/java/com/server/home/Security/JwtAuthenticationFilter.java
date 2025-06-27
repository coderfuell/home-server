package com.server.home.Security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import com.server.home.Services.AuthService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthService authService;

    public JwtAuthenticationFilter(AuthService authService ) {
        this.authService = authService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/user/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterchain) throws IOException, ServletException{
        try{
        authService.authenticate(request, response);
        filterchain.doFilter(request, response);
        }
        catch(ExpiredJwtException e){
            response.setStatus(HttpStatus.GATEWAY_TIMEOUT.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + e.getMessage() +  "\"}");
        }
    }
}
