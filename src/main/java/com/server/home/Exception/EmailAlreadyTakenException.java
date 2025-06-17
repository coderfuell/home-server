package com.server.home.Exception;

public class EmailAlreadyTakenException extends RuntimeException{
    public EmailAlreadyTakenException(String message){
        super(message);
    }

    public EmailAlreadyTakenException(){}
    
}
