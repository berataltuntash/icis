package com.icis.demo.Utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtil {
    private static SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public JWTUtil(@Value("${jwt.token}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public static String createJWTToken(String username){
        long currentTime = System.currentTimeMillis();
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + 1000*60*60*24)) // Token expires in 24 hours
                .signWith(secretKey)
                .compact();

        return token;
    }

    public boolean validateJWTToken(String token, String username){
        final String tokenUsername = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
        if(!tokenUsername.equals(username)){
            return false;
        }
        // Check token expiration
        if(new Date().after(Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getExpiration())){
            return false;
        }
        return true;
    }
}
