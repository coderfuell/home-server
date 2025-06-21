package com.server.home.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.server.home.Dto.LoginDto;
import com.server.home.Model.User;
import com.server.home.Services.CustomUserDetailsService;
import com.server.home.Services.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtService jwtService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/signup")
    public void signup(@RequestBody User user) {
        userDetailsService.createUser(user);
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        User user;
        if (loginDto.getUsername() == null) {
            user = userDetailsService.verifyUserByEmail(loginDto.getEmail(), loginDto.getPassword());
        } else {
            user = userDetailsService.verifyUserByUsername(loginDto.getUsername(), loginDto.getPassword());
        }
        Cookie cookie = jwtService.generateCookie(user);
        response.addCookie(cookie);
    }
}
