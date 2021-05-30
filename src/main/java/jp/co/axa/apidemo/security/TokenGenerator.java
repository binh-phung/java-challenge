package jp.co.axa.apidemo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class TokenGenerator {
    private final String secret;
    private final long expiredTime;
    TokenGenerator(String secret,long expiredTime){
        this.secret = secret;
        this.expiredTime = expiredTime;
    }

    public String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();
    }
}
