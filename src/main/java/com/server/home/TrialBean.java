package com.server.home;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TrialBean {
    @Value("${jwt.secret.key}")
    private String pass;

    public String getPass() {
        return pass;
    }
}
