package com.example.formativa.backend;

import java.io.IOException;
import java.util.Arrays;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JWTAuthorizationFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private JwtParserBuilder jwtParserBuilder;

    @Mock
    private JwtParser jwtParser;

    @Mock
    private SecretKey secretKey;

    @Mock
    private Constants constants;

    @InjectMocks
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
        // System.setProperty("JWT_SECRET", "testSecretKey");
    }

    @Test
    void testIsJWTValid_ValidToken() {
        when(request.getHeader("Authorization"))
            .thenReturn("Bearer " + "valid.token.here");
        
        assertTrue(jwtAuthorizationFilter.isJWTValid(request, response));
    }

    @Test
    void testIsJWTValid_NoToken() {
        when(request.getHeader("Authorization"))
            .thenReturn(null);
        
        assertFalse(jwtAuthorizationFilter.isJWTValid(request, response));
    }

    @Test
    void testIsJWTValid_InvalidPrefix() {
        when(request.getHeader("Authorization"))
            .thenReturn("InvalidPrefix valid.token.here");
        
        assertFalse(jwtAuthorizationFilter.isJWTValid(request, response));
    }

    @Test
    void testSetSigningKey_ValidToken() {
        String token = "valid.token.here";
        when(request.getHeader("Authorization"))
            .thenReturn("Bearer " + token);

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("testUser");
        when(claims.get("authorities")).thenReturn(Arrays.asList("ROLE_USER"));

        // Mock JWT parser
        try (var mockedStatic = mockStatic(Jwts.class)) {
            mockedStatic.when(() -> Jwts.parser()).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.verifyWith(any(SecretKey.class))).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.build()).thenReturn(jwtParser);
            when(jwtParser.parseSignedClaims(anyString())).thenReturn(mock(io.jsonwebtoken.Jws.class));
            when(jwtParser.parseSignedClaims(anyString()).getPayload()).thenReturn(claims);

            // when(constants.getSuperSecretSigningKey()).thenReturn("testSecretKey");
            Claims result = jwtAuthorizationFilter.setSigningKey(request);
            assertNotNull(result);
            assertEquals("testUser", result.getSubject());
            assertEquals(Arrays.asList("ROLE_USER"), result.get("authorities"));
        }
    }

    @Test
    void testSetAuthentication_ValidAuthorities() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("testUser");
        when(claims.get("authorities")).thenReturn(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));

        jwtAuthorizationFilter.setAuthentication(claims);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("testUser", SecurityContextHolder.getContext().getAuthentication().getName());
        assertEquals(2, SecurityContextHolder.getContext().getAuthentication().getAuthorities().size());
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        when(request.getHeader(Constants.HEADER_AUTHORIZACION_KEY))
            .thenReturn(Constants.TOKEN_BEARER_PREFIX + "valid.token.here");

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("testUser");
        when(claims.get("authorities")).thenReturn(Arrays.asList("ROLE_USER"));

        try (var mockedStatic = mockStatic(Jwts.class)) {
            mockedStatic.when(() -> Jwts.parser()).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.verifyWith(any(SecretKey.class))).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.build()).thenReturn(jwtParser);
            when(jwtParser.parseSignedClaims(anyString())).thenReturn(mock(io.jsonwebtoken.Jws.class));
            when(jwtParser.parseSignedClaims(anyString()).getPayload()).thenReturn(claims);

            // when(constants.getSuperSecretSigningKey()).thenReturn("testSecretKey");
            jwtAuthorizationFilter.setSigningKey(request);
            jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        }
    }

    @Test
    void testDoFilterInternal_ExpiredToken() throws ServletException, IOException {
        when(request.getHeader(Constants.HEADER_AUTHORIZACION_KEY))
            .thenReturn(Constants.TOKEN_BEARER_PREFIX + "expired.token.here");

        try (var mockedStatic = mockStatic(Jwts.class)) {
            mockedStatic.when(() -> Jwts.parser()).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.verifyWith(any(SecretKey.class))).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.build()).thenReturn(jwtParser);
            when(jwtParser.parseSignedClaims(anyString())).thenThrow(new ExpiredJwtException(null, null, "Token expired"));

            jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

            verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
            verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Token expired");
        }
    }

    @Test
    void testDoFilterInternal_MalformedToken() throws ServletException, IOException {
        when(request.getHeader(Constants.HEADER_AUTHORIZACION_KEY))
            .thenReturn(Constants.TOKEN_BEARER_PREFIX + "malformed.token.here");

        try (var mockedStatic = mockStatic(Jwts.class)) {
            mockedStatic.when(() -> Jwts.parser()).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.verifyWith(any(SecretKey.class))).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.build()).thenReturn(jwtParser);
            when(jwtParser.parseSignedClaims(anyString())).thenThrow(new MalformedJwtException("Invalid token format"));

            jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

            verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
            verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid token format");
        }
    }

    @Test
    void testDoFilterInternal_UnsupportedToken() throws ServletException, IOException {
        when(request.getHeader(Constants.HEADER_AUTHORIZACION_KEY))
            .thenReturn(Constants.TOKEN_BEARER_PREFIX + "unsupported.token.here");

        try (var mockedStatic = mockStatic(Jwts.class)) {
            mockedStatic.when(() -> Jwts.parser()).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.verifyWith(any(SecretKey.class))).thenReturn(jwtParserBuilder);
            when(jwtParserBuilder.build()).thenReturn(jwtParser);
            when(jwtParser.parseSignedClaims(anyString())).thenThrow(new UnsupportedJwtException("Unsupported token"));

            jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

            verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
            verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Unsupported token");
        }
    }

    @Test
    void testDoFilterInternal_NoToken() throws ServletException, IOException {
        when(request.getHeader(Constants.HEADER_AUTHORIZACION_KEY))
            .thenReturn(null);

        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
} 