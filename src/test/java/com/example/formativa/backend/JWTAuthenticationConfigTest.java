package com.example.formativa.backend;

import java.security.Key;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JWTAuthenticationConfigTest {

    private JWTAuthenticationConfig config;

    @BeforeEach
    void setUp() {
        config = new JWTAuthenticationConfig();
    }

    @Test
    void testGetJWTToken_WithNullUsername() {
        assertThrows(NullPointerException.class, () -> {
            config.getJWTToken(null);
        });
    }

    @Test
    void testGetJWTToken_WithValidUsername() {
        String username = "testuser";
        String token = config.getJWTToken(username);

        assertNotNull(token);
        assertTrue(token.startsWith("Bearer "));
        
        String jwtToken = token.substring(7);
        
        Key signingKey = Constants.getSigningKey(Constants.SUPER_SECRET_KEY);
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) signingKey)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();

        assertEquals(username, claims.getSubject());
        
        @SuppressWarnings("unchecked")
        List<String> authorities = (List<String>) claims.get("authorities");
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_USER", authorities.get(0));

        Date expiration = claims.getExpiration();
        assertNotNull(expiration);
        Date now = new Date();
        assertTrue(expiration.after(now));
        assertTrue(expiration.before(new Date(now.getTime() + 1000L * 60 * 1440 + 1000)));
    }

    @Test
    void testGetJWTToken_WithEmptyUsername() {
        String token = config.getJWTToken("");
        
        assertNotNull(token);
        assertTrue(token.startsWith("Bearer "));
        
        String jwtToken = token.substring(7);
        Key signingKey = Constants.getSigningKey(Constants.SUPER_SECRET_KEY);
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) signingKey)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();

    }

    @Test
    void testGetJWTToken_WithSpecialCharacters() {
        String username = "test@user.com";
        String token = config.getJWTToken(username);
        
        assertNotNull(token);
        assertTrue(token.startsWith("Bearer "));
        
        String jwtToken = token.substring(7);
        Key signingKey = Constants.getSigningKey(Constants.SUPER_SECRET_KEY);
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) signingKey)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();

        assertEquals(username, claims.getSubject());
    }
}
