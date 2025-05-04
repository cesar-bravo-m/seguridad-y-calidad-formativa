package com.example.formativa.backend;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import static com.example.formativa.backend.Constants.SUPER_SECRET_KEY;
import static com.example.formativa.backend.Constants.getSigningKey;

import io.jsonwebtoken.Jwts;

@Configuration
public class JWTAuthenticationConfig {

    public String getJWTToken(String username) {
        if (username == null) {
            throw new NullPointerException("Username is null");
        }
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        List<String> roles = grantedAuthorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", roles);

        String token = Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 1440))
                .and()
                .signWith(getSigningKey(SUPER_SECRET_KEY))
                .compact();

        return "Bearer " + token;
    }

}

