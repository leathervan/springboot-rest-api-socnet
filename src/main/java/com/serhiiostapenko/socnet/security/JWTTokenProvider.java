package com.serhiiostapenko.socnet.security;

import com.serhiiostapenko.socnet.entity.Person;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JWTTokenProvider {

    public String generateToken(Authentication authentication){
        Person person = (Person) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expiration = new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME);

        String personId = String.valueOf(person.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", personId);
        claims.put("username", person.getUsername());
        claims.put("email", person.getEmail());

        return Jwts.builder()
                .setSubject(personId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(token);

            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            log.error("Could not validate token: " + e.getMessage());
            return false;
        }
    }

    public Long getPersonIdFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token)
                .getBody();

        String id = (String) claims.get("id");
        return Long.parseLong(id);

    }
}
