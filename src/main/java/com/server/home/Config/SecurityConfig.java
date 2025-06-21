package com.server.home.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import com.server.home.Security.JwtAuthenticationFilter;
import com.server.home.Services.CustomUserDetailsService;
import com.server.home.Services.JwtService;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService){
        return new JwtAuthenticationFilter(jwtService, userDetailsService);
    }

    @Bean
    @Order(1)
    public SecurityFilterChain directoriesSecurityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        SecurityFilterChain chain = http
        .securityMatcher("/directories/**")
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .addFilterAfter(jwtAuthenticationFilter, LogoutFilter.class)
        .csrf(csrf -> csrf.disable())
        .build();
        return chain;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception{
        SecurityFilterChain chain = http
        .securityMatcher("/user/**")
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .csrf(csrf->csrf.disable())
        .build();

        return chain;
    }
}
