package com.example.formativa.controllers;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.formativa.backend.JWTAuthenticationConfig;
import com.example.formativa.services.UserService;
import com.example.formativa.config.TestSecurityConfig;

import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.servlet.ServletException;
import org.springframework.test.context.ActiveProfiles;


@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
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
        MockitoAnnotations.openMocks(this);
        userDetails = new User(TEST_USERNAME, TEST_PASSWORD, new ArrayList<>());
        when(userService.loadUserByUsername(TEST_USERNAME)).thenReturn(userDetails);
        when(passwordEncoder.matches(TEST_PASSWORD, TEST_PASSWORD)).thenReturn(true);
        when(jwtAuthenticationConfig.getJWTToken(TEST_USERNAME)).thenReturn(TEST_TOKEN);
    }

    @Test
    @DisplayName("GET /login should display the login page")
    void showLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /login with invalid password should throw exception")
    void loginWithInvalidPassword() throws Exception {
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // expect RuntimeException "invalid login"
        Exception exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/login")
                    .param("user", TEST_USERNAME)
                    .param("encryptedPass", "wrongpassword"));
        });
        // assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("POST /login with non-existent user should throw exception")
    void loginWithNonExistentUser() throws Exception {
        when(userService.loadUserByUsername(anyString()))
            .thenThrow(new RuntimeException("User not found"));
        
        Exception exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/login")
                    .param("user", "nonexistentuser")
                    .param("encryptedPass", TEST_PASSWORD));
        });
        // assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("POST /login with valid credentials should return token")
    void loginWithValidCredentials() throws Exception {
        when(userService.loadUserByUsername(anyString())).thenReturn(userDetails);
        mockMvc.perform(post("/login")
                .param("user", TEST_USERNAME)
                .param("encryptedPass", TEST_PASSWORD))
               .andExpect(status().isFound())
               .andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer " + TEST_TOKEN))
               .andExpect(redirectedUrl("/"));
    }
}