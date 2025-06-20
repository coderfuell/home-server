package com.server.home;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;



public class Trial {
   
    public static void main(String[] args) {
        SecretKey key = Jwts.SIG.HS256.key().build();
        String s = Encoders.BASE64.encode(key.getEncoded());
        System.out.println(s);
        


        // Password pas = Keys.password(tr.secretKey.toCharArray());
        // System.out.println(pas);


    }
    
}
