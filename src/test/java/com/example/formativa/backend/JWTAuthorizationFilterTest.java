package com.example.formativa.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.servlet.FilterChain;

class JWTAuthorizationFilterTest {

    private JWTAuthorizationFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        filter = new JWTAuthorizationFilter();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Prueba si JWT es válido")
    void testIsJWTValid() throws Exception {
        request.addHeader("Authorization", "Bearer " + "token");
        boolean result = filter.isJWTValid(request, response);
        assertTrue(result);
    }

    @Test
    @DisplayName("Rechaza petición con formato de token inválido")
    void testInvalidJWTTokenFormat() throws Exception {
        request.addHeader("Authorization", "InvalidToken");
        filter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }

    @Test
    @DisplayName("Rechaza petición sin encabezado de autorización")
    void testNoAuthorizationHeader() throws Exception {
        filter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }
} 