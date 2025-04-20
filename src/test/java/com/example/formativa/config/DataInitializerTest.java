package com.example.formativa.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.formativa.models.Profile;
import com.example.formativa.models.User;
import com.example.formativa.repositories.ProfileRepository;
import com.example.formativa.repositories.UserRepository;
import com.example.formativa.services.UserService;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private UserService userService;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private ProfileRepository profileRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private DataInitializer dataInitializer;
    
    private User testUser;
    private Profile testProfile;
    
    @BeforeEach
    void setUp() {
        testUser = new User("user1", "encodedPassword", "user1@example.com");
        testProfile = new Profile(testUser);
        testProfile.setAvatarUri("https://example.com/avatar.jpg");
        testProfile.setFavoriteGames("Game1,Game2");
        testProfile.setEmailNotifications(true);
        testProfile.setPushNotifications(false);
        testUser.setProfile(testProfile);
    }
    
    @Test
    @DisplayName("Debería no inicializar usuarios cuando ya existen")
    void testDoNotInitializeUsersWhenTheyExist() throws Exception {
        when(userRepository.existsByUsername("user1")).thenReturn(true);
        when(userRepository.existsByUsername("user2")).thenReturn(true);
        when(userRepository.existsByUsername("user3")).thenReturn(true);
        when(userRepository.existsByUsername("user4")).thenReturn(true);
        
        dataInitializer.run();
        
        verify(userService, never()).registerNewUser(anyString(), anyString(), anyString(), any(PasswordEncoder.class));
        verify(profileRepository, never()).save(any(Profile.class));
    }
    
    @Test
    @DisplayName("Debería actualizar el perfil con los valores correctos")
    void testUpdateProfile() throws Exception {
        User user = new User("testuser", "password", "test@example.com");
        Profile profile = new Profile(user);
        user.setProfile(profile);
        
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);
        
        java.lang.reflect.Method method = DataInitializer.class.getDeclaredMethod("updateProfile", 
            User.class, String.class, String.class, boolean.class, boolean.class);
        method.setAccessible(true);
        method.invoke(dataInitializer, user, "https://example.com/avatar.jpg", "Game1,Game2", true, false);
        
        assertEquals("https://example.com/avatar.jpg", profile.getAvatarUri());
        assertEquals("Game1,Game2", profile.getFavoriteGames());
        assertTrue(profile.isEmailNotifications());
        assertFalse(profile.isPushNotifications());
        
        verify(profileRepository).save(profile);
    }
} 