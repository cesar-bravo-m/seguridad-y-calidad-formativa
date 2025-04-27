package com.example.formativa.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.security.Principal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.example.formativa.models.Profile;
import com.example.formativa.models.User;
import com.example.formativa.services.UserService;

@WebMvcTest(ProfileController.class)
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private Principal principal;
    private User testUser;
    private Profile testProfile;

    @BeforeEach
    void setUp() {
        principal = () -> "testuser";
        testUser = new User();
        testUser.setUsername("testuser");
        testProfile = new Profile();
        testProfile.setUser(testUser);
    }

    @Test
    @DisplayName("GET /profile debe mostrar la página de perfil cuando el usuario está autenticado y tiene un perfil")
    void showProfilePage_WhenUserAuthenticated_ShouldShowProfile() throws Exception {
        when(userService.getProfileByUsername("testuser")).thenReturn(Optional.of(testProfile));

        mockMvc.perform(get("/profile")
                .principal(principal))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /profile debe mostrar un perfil vacío cuando el perfil no existe")
    void showProfilePage_WhenProfileDoesNotExist_ShouldShowEmptyProfile() throws Exception {
        when(userService.getProfileByUsername("testuser")).thenReturn(Optional.empty());

        mockMvc.perform(get("/profile")
                .principal(principal))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /profile/update debe fallar cuando el nuevo nombre de usuario ya existe")
    void updateProfile_WhenUsernameExists_ShouldFail() throws Exception {
        when(userService.findByUsername("existinguser")).thenReturn(Optional.of(new User()));

        mockMvc.perform(multipart("/profile/update")
                .principal(principal)
                .param("username", "existinguser")
                .param("games", "Minecraft"))
                .andExpect(status().is4xxClientError());
    }
}
