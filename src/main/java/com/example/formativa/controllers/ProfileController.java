package com.example.formativa.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.formativa.models.Profile;
import com.example.formativa.services.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String showProfile(Model model, Principal principal) {
        String username = principal.getName();
        Profile profile = userService.getProfileByUsername(username)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + username));
        
        model.addAttribute("profile", profile);
        return "profile";
    }
    
    @PostMapping("/update")
    public String updateProfile(
            Principal principal,
            @RequestParam(required = true) String username,
            @RequestParam(required = false) String games,
            @RequestParam(required = false) MultipartFile avatar,
            @RequestParam(value = "notifications", required = false) String[] notifications,
            RedirectAttributes redirectAttributes) {
        
        String currentUsername = principal.getName();
        
        // Check if username already exists (if it's being changed)
        if (!currentUsername.equals(username) && userService.findByUsername(username).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El nombre de usuario ya existe");
            return "redirect:/profile";
        }
        
        // Handle avatar upload (in a real app, you'd save the file and get a URL)
        String avatarUri = null;
        if (avatar != null && !avatar.isEmpty()) {
            // This is a simplified example - in a real app, you'd save the file and get a URL
            avatarUri = "/uploads/avatars/" + username + "_" + avatar.getOriginalFilename();
        }
        
        boolean emailNotifications = false;
        boolean pushNotifications = false;
        
        if (notifications != null) {
            for (String notif : notifications) {
                if ("email".equals(notif)) {
                    emailNotifications = true;
                } else if ("push".equals(notif)) {
                    pushNotifications = true;
                }
            }
        }
        
        // Update the profile with the new username
        userService.updateProfile(currentUsername, avatarUri, games, emailNotifications, pushNotifications);
        
        redirectAttributes.addFlashAttribute("success", "Perfil actualizado correctamente");
        
        // If username was changed, redirect to logout to force re-authentication
        // if (!currentUsername.equals(username)) {
        //     return "redirect:/logout";
        // }
        
        return "redirect:/profile";
    }
} 