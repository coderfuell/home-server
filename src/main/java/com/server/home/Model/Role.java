package com.server.home.Model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{
    READ,
    WRITE,
    ADMIN;

    @Override
    public String getAuthority(){
        return name();
    }
}
