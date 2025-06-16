package com.server.home.Services;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.server.home.Model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.time.duration}")
    private Long timeDuration;

    public String generateJwt(User user){
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        Date currentTime = new Date();
        Date expirationTime = new Date(currentTime.getTime() + timeDuration);

        String jws = Jwts.builder()
        .issuer(user.getUsername())
        .expiration(expirationTime)
        .signWith(key)
        .compact();

        return jws;
    }

    public Claims validateJwt(String jwt) throws JwtException{
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        Jws<Claims> jws = Jwts.parser()
        .verifyWith(key)
        .clockSkewSeconds(180)
        .build()
        .parseSignedClaims(jwt);
        Claims claims = jws.getPayload();
        return claims;
    }
}
