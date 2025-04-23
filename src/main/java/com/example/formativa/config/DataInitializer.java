package com.example.formativa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.formativa.models.Profile;
import com.example.formativa.models.User;
import com.example.formativa.repositories.ProfileRepository;
import com.example.formativa.repositories.UserRepository;
import com.example.formativa.services.UserService;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByUsername("user1")) {
            User user1 = userService.registerNewUser("user1", "user1@example.com", "pass1", passwordEncoder);
            updateProfile(user1, "https://placehold.co/150x150/9D4EDD/ffffff?text=User1", 
                         "League of Legends,Valorant,Minecraft", true, true);
            System.out.println("Created user: user1");
        }
        
        if (!userRepository.existsByUsername("user2")) {
            User user2 = userService.registerNewUser("user2", "user2@example.com", "pass2", passwordEncoder);
            updateProfile(user2, "https://placehold.co/150x150/7B2CBF/ffffff?text=User2", 
                         "Fortnite,Call of Duty,FIFA 24", true, false);
            System.out.println("Created user: user2");
        }
        
        if (!userRepository.existsByUsername("user3")) {
            User user3 = userService.registerNewUser("user3", "user3@example.com", "pass3", passwordEncoder);
            updateProfile(user3, "https://placehold.co/150x150/5A189A/ffffff?text=User3", 
                         "Rocket League,Super Smash Bros,Mario Kart", false, true);
            System.out.println("Created user: user3");
        }
        
        if (!userRepository.existsByUsername("user4")) {
            User user4 = userService.registerNewUser("user4", "user4@example.com", "pass4", passwordEncoder);
            updateProfile(user4, "https://placehold.co/150x150/3C096C/ffffff?text=User4", 
                         "Minecraft,Among Us,Hearthstone", false, false);
            System.out.println("Created user: user4");
        }
    }
    
    private void updateProfile(User user, String avatarUri, String favoriteGames, 
                              boolean emailNotifications, boolean pushNotifications) {
        Profile profile = user.getProfile();
        profile.setAvatarUri(avatarUri);
        profile.setFavoriteGames(favoriteGames);
        profile.setEmailNotifications(emailNotifications);
        profile.setPushNotifications(pushNotifications);
        profileRepository.save(profile);
    }
} 