package com.example.formativa.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.formativa.repositories.UserRepository;
import com.example.formativa.services.UserService;

@WebMvcTest(RegisterController.class)
@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testShowRegisterForm() throws Exception {
        mockMvc.perform(get("/register"))
               .andExpect(status().isOk())
               .andExpect(view().name("register"));
    }

    @Test
    void testRegisterUser_InvalidEmail() throws Exception {
        mockMvc.perform(post("/register")
                .param("username", "testuser")
                .param("name", "Test User")
                .param("email", "invalid-email")
                .param("password", "password123")
                .param("confirmPassword", "password123"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/register"))
               .andExpect(flash().attributeExists("error"))
               .andExpect(flash().attribute("error", "Invalid email format"));
    }

    @Test
    void testRegisterUser_PasswordsDoNotMatch() throws Exception {
        mockMvc.perform(post("/register")
                .param("username", "testuser")
                .param("name", "Test User")
                .param("email", "test@example.com")
                .param("password", "password123")
                .param("confirmPassword", "differentPassword"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/register"))
               .andExpect(flash().attributeExists("error"))
               .andExpect(flash().attribute("error", "Passwords do not match"));
    }

    @Test
    void testRegisterUser_UsernameAlreadyExists() throws Exception {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        mockMvc.perform(post("/register")
                .param("username", "testuser")
                .param("name", "Test User")
                .param("email", "test@example.com")
                .param("password", "password123")
                .param("confirmPassword", "password123"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/register"))
               .andExpect(flash().attributeExists("error"))
               .andExpect(flash().attribute("error", "Username already exists"));
    }

    @Test
    void testRegisterUser_SuccessfulRegistration() throws Exception {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);

        mockMvc.perform(post("/register")
                .param("username", "testuser")
                .param("name", "Test User")
                .param("email", "test@example.com")
                .param("password", "password123")
                .param("confirmPassword", "password123"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/login"))
               .andExpect(flash().attributeExists("success"))
               .andExpect(flash().attribute("success", "Registration successful! Please login."));

        verify(userService).registerNewUser(eq("testuser"), eq("test@example.com"), eq("password123"), any(PasswordEncoder.class));
    }
} 