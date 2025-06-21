package com.server.home.Services;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.server.home.Model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;

@Service
public class JwtService {
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.time.duration}")
    private Long timeDuration;

    private String generateBearer(User user){
        String jwt = generateJwt(user);
        return "Bearer " + jwt;
    }

    private String generateJwt(User user){
        SecretKey key = getKey();
        Date currentTime = new Date();
        Date expirationTime = new Date(currentTime.getTime() + timeDuration);

        String jws = Jwts.builder()
        .issuer(user.getUsername())
        .expiration(expirationTime)
        .signWith(key)
        .compact();

        return jws;
    }

    public Cookie generateCookie(User user){
        String bearer = generateBearer(user);
        Cookie cookie = new Cookie("Authorization", bearer);
        cookie.setHttpOnly(true);
        cookie.setPath("/directories/");
        cookie.setMaxAge(60*60*24*365);
        return cookie;
    }

    private Claims extractClaims(String jwt) throws JwtException{
        SecretKey key = getKey();
        Jws<Claims> jws = Jwts.parser()
            .verifyWith(key)
            .clockSkewSeconds(180)
            .build()
            .parseSignedClaims(jwt);
        Claims claims = jws.getPayload();
        return claims;
    }

    public String getUsername(Cookie[] cookies){
        String bearer = null;
        if (cookies != null){
            for (Cookie cookie: cookies){
                if (cookie.getName().equals("Authorization")){
                    bearer = cookie.getValue();
                    break;
                }
            }
        }
        if (bearer == null || !bearer.startsWith("Bearer ")){
            throw new BadCredentialsException("no bearer token passed");
        }
        String username = getUsername(bearer);
        return username;
    }

    private String getUsername(String bearer){
        String jwt = bearer.substring(7);
        Claims claims = extractClaims(jwt);
        return claims.getIssuer();
    }

    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
    

}
