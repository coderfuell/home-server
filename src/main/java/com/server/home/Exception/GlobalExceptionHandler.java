package com.server.home.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(value = HttpStatus.CONFLICT, reason="username already exists")
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public void usernameAlreadyExistsException(){}

    @ResponseStatus(value = HttpStatus.CONFLICT, reason="username already exists")
    @ExceptionHandler(EmailAlreadyTakenException.class)
    public void emailAlreadyTakenException(){}

    
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> userNotExistException(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> expiredJwtException(Exception e) {
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(e.getMessage());
    }
}
