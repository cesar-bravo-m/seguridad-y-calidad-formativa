package com.example.formativa.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.example.formativa.backend.JWTAuthenticationConfig;
import com.example.formativa.services.UserService;

import java.util.ArrayList;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JWTAuthenticationConfig jwtAuthenticationConfig;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private UserDetails userDetails;
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_TOKEN = "test.jwt.token";

    @BeforeEach
    void setUp() {
        userDetails = new User(TEST_USERNAME, TEST_PASSWORD, new ArrayList<>());
        when(userService.loadUserByUsername(TEST_USERNAME)).thenReturn(userDetails);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtAuthenticationConfig.getJWTToken(anyString())).thenReturn(TEST_TOKEN);
    }

    @Test
    @DisplayName("POST /login con contraseña inválida debe lanzar excepción")
    void loginWithInvalidPassword() throws Exception {
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/login")
                .param("user", TEST_USERNAME)
                .param("encryptedPass", "wrongpassword"))
               .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /login con usuario inexistente debe lanzar excepción")
    void loginWithNonExistentUser() throws Exception {
        when(userService.loadUserByUsername(anyString()))
            .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(post("/login")
                .param("user", "nonexistentuser")
                .param("encryptedPass", TEST_PASSWORD))
               .andExpect(status().isForbidden());
    }
} 