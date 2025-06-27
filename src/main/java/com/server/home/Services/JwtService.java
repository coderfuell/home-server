package com.server.home.Services;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.server.home.Model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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

    @Value("${jwt.refresh.key}")
    private String refreshKey;

    @Value("${jwt.time.duration}")
    private Long timeDuration;

    @Value("${jwt.refresh.duration}")
    private Long refreshDuration;

    private String generateBearer(User user){
        String jwt = generateJwt(user, secretKey, timeDuration);
        return "Bearer-" + jwt;
    }

    private String generateRefresh(User user){
        String refresh = generateJwt(user, refreshKey, refreshDuration);
        return refresh;
    }

    private String generateJwt(User user, String tokenKey, Long duration){
        SecretKey key = getKey(tokenKey);
        Date currentTime = new Date();
        Date expirationTime = new Date(currentTime.getTime() + duration);

        String jws = Jwts.builder()
        .issuer(user.getUsername())
        .expiration(expirationTime)
        .signWith(key)
        .compact();

        return jws;
    }

    public Cookie generateAccessCookie(User user){
        String bearer = generateBearer(user);
        return generateCookie("Authorization", bearer);
    }

    public Cookie generateRefreshCookie(User user){
        String refreshToken = generateRefresh(user);
        return generateCookie("Refresh", refreshToken);
    }

    private Cookie generateCookie(String cookieName, String token){
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/directories/");
        cookie.setMaxAge(60 * 60 * 24 * 365);
        return cookie;
    }


    public String validateRefreshToken(Cookie[] cookies){
        String refreshToken = getCookieValue(cookies, "Refresh");
        if (refreshToken == null) {
            throw new BadCredentialsException("no refresh token passed");
        }
        Claims claims;
        try{
        claims = extractClaims(refreshToken, refreshKey);}
        catch (ExpiredJwtException e){
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "Refresh Token Expired: " + e.getMessage());
        }
        return claims.getIssuer();
    }

    public String getUsername(Cookie[] cookies){
        String bearer = getCookieValue(cookies, "Authorization");
        if (bearer == null || !bearer.startsWith("Bearer-")){
            throw new BadCredentialsException("no bearer token passed");
        }
        String username = getUsername(bearer);
        return username;
    }

    private String getCookieValue(Cookie[] cookies, String cookieName){
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }

    private String getUsername(String bearer){
        String jwt = bearer.substring(7);
        Claims claims = extractClaims(jwt, secretKey);
        return claims.getIssuer();
    }

    private Claims extractClaims(String jwt, String tokenKey) throws JwtException {
        SecretKey key = getKey(tokenKey);
        Jws<Claims> jws = Jwts.parser()
                .verifyWith(key)
                .clockSkewSeconds(0)
                .build()
                .parseSignedClaims(jwt);
        Claims claims = jws.getPayload();
        return claims;
    }

    private SecretKey getKey(String key){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }
    

}
