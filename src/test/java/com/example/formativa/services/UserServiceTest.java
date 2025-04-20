package com.example.formativa.services;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.formativa.models.Event;
import com.example.formativa.repositories.EventRepository;
import com.example.formativa.repositories.UserRepository;
import com.example.formativa.models.User;
import com.example.formativa.models.Profile;
import com.example.formativa.repositories.ProfileRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Profile testProfile;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setEmail("testuser@example.com");

        testProfile = new Profile();
        testProfile.setUser(testUser);
        testProfile.setAvatarUri("avatar.jpg");
        testProfile.setFavoriteGames("Game1, Game2");
        testProfile.setEmailNotifications(true);
        testProfile.setPushNotifications(true);
    }

    @Test
    @DisplayName("Buscar por nombre de usuario")
    void findByUsername_ShouldReturnUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        Optional<User> result = userService.findByUsername("testuser");
        assertEquals(testUser, result.get());
    }

    @Test
    @DisplayName("Obtener perfil por nombre de usuario")
    void getProfileByUsername_ShouldReturnProfile() {
        when(profileRepository.findByUserUsername("testuser")).thenReturn(Optional.of(testProfile));
        Optional<Profile> result = userService.getProfileByUsername("testuser");
        assertEquals(testProfile, result.get());
    }
}
