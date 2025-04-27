package com.example.formativa.services;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.formativa.models.User;
import com.example.formativa.models.Profile;
import com.example.formativa.repositories.UserRepository;
import com.example.formativa.repositories.ProfileRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Profile testProfile;
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_EMAIL = "testuser@example.com";
    private static final String TEST_AVATAR = "avatar.jpg";
    private static final String TEST_FAVORITE_GAMES = "Game1, Game2";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername(TEST_USERNAME);
        testUser.setPassword(TEST_PASSWORD);
        testUser.setEmail(TEST_EMAIL);

        testProfile = new Profile();
        testProfile.setUser(testUser);
        testProfile.setAvatarUri(TEST_AVATAR);
        testProfile.setFavoriteGames(TEST_FAVORITE_GAMES);
        testProfile.setEmailNotifications(true);
        testProfile.setPushNotifications(true);
    }

    @Test
    @DisplayName("loadUserByUsername - Éxito")
    void loadUserByUsername_ShouldReturnUserDetails() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));
        
        UserDetails userDetails = userService.loadUserByUsername(TEST_USERNAME);
        
        assertNotNull(userDetails);
        assertEquals(TEST_USERNAME, userDetails.getUsername());
        assertEquals(TEST_PASSWORD, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("loadUserByUsername - User Not Found")
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
        
        assertThrows(UsernameNotFoundException.class, () -> 
            userService.loadUserByUsername(TEST_USERNAME)
        );
    }

    @Test
    @DisplayName("registerNewUser - Success")
    void registerNewUser_ShouldCreateUserAndProfile() {
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        User result = userService.registerNewUser(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD, passwordEncoder);

        assertNotNull(result);
        assertEquals(TEST_USERNAME, result.getUsername());
        assertEquals(TEST_EMAIL, result.getEmail());

        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    @DisplayName("findByUsername - Success")
    void findByUsername_ShouldReturnUser() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));
        
        Optional<User> result = userService.findByUsername(TEST_USERNAME);
        
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
    }

    @Test
    @DisplayName("findByUsername - User Not Found")
    void findByUsername_ShouldReturnEmpty_WhenUserNotFound() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
        
        Optional<User> result = userService.findByUsername(TEST_USERNAME);
        
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getProfileByUsername - Success")
    void getProfileByUsername_ShouldReturnProfile() {
        when(profileRepository.findByUserUsername(TEST_USERNAME)).thenReturn(Optional.of(testProfile));
        
        Optional<Profile> result = userService.getProfileByUsername(TEST_USERNAME);
        
        assertTrue(result.isPresent());
        assertEquals(testProfile, result.get());
    }

    @Test
    @DisplayName("getProfileByUsername - Profile Not Found")
    void getProfileByUsername_ShouldReturnEmpty_WhenProfileNotFound() {
        when(profileRepository.findByUserUsername(TEST_USERNAME)).thenReturn(Optional.empty());
        
        Optional<Profile> result = userService.getProfileByUsername(TEST_USERNAME);
        
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("createProfile - Success")
    void createProfile_ShouldCreateNewProfile() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        userService.createProfile(TEST_USERNAME, TEST_AVATAR, TEST_FAVORITE_GAMES, true, true);

        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    @DisplayName("createProfile - User Not Found")
    void createProfile_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
            userService.createProfile(TEST_USERNAME, TEST_AVATAR, TEST_FAVORITE_GAMES, true, true)
        );
    }

    @Test
    @DisplayName("updateProfile - Success")
    void updateProfile_ShouldUpdateExistingProfile() {
        when(profileRepository.findByUserUsername(TEST_USERNAME)).thenReturn(Optional.of(testProfile));
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        Profile result = userService.updateProfile(TEST_USERNAME, TEST_AVATAR, TEST_FAVORITE_GAMES, true, true);

        assertNotNull(result);
        assertEquals(TEST_AVATAR, result.getAvatarUri());
        assertEquals(TEST_FAVORITE_GAMES, result.getFavoriteGames());
        assertTrue(result.isEmailNotifications());
        assertTrue(result.isPushNotifications());
    }

    @Test
    @DisplayName("updateProfile - Profile Not Found")
    void updateProfile_ShouldThrowException_WhenProfileNotFound() {
        when(profileRepository.findByUserUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
            userService.updateProfile(TEST_USERNAME, TEST_AVATAR, TEST_FAVORITE_GAMES, true, true)
        );
    }

    @Test
    @DisplayName("updateUsername - Success")
    void updateUsername_ShouldUpdateUsername() {
        String newUsername = "newusername";
        when(profileRepository.findByUserUsername(TEST_USERNAME)).thenReturn(Optional.of(testProfile));
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        User result = userService.updateUsername(TEST_USERNAME, newUsername);

        assertNotNull(result);
        assertEquals(newUsername, result.getUsername());
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    @DisplayName("updateUsername - Profile Not Found")
    void updateUsername_ShouldThrowException_WhenProfileNotFound() {
        when(profileRepository.findByUserUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
            userService.updateUsername(TEST_USERNAME, "newusername")
        );
    }

    @Test
    @DisplayName("updateUsername - User Not Found")
    void updateUsername_ShouldThrowException_WhenUserNotFound() {
        when(profileRepository.findByUserUsername(TEST_USERNAME)).thenReturn(Optional.of(testProfile));
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
            userService.updateUsername(TEST_USERNAME, "newusername")
        );
    }
}
