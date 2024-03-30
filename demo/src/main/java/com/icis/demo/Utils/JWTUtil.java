package com.icis.demo.Utils;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.antlr.v4.runtime.Token;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {
    private String secretKey = "secretKey";

    public String createJWTToken(String username){
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + 1000*60*60*24))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    public boolean validateJWTToken(String token, String username){
        final String tokenUsername = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        if(!tokenUsername.equals(username)){
            return false;
        }
        if(new Date().after(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration())){
            return false;
        }
        return true;
    }
}
