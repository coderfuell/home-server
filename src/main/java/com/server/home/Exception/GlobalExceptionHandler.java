package com.server.home.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(value = HttpStatus.CONFLICT, reason="username already exists")
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public void usernameAlreadyExistsException(){}

    @ResponseStatus(value = HttpStatus.CONFLICT, reason="username already exists")
    @ExceptionHandler(EmailAlreadyTakenException.class)
    public void emailAlreadyTakenException(){}

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "user not found")
    @ExceptionHandler(UsernameNotFoundException.class)
    public void userNotExistException() {
    }
}
