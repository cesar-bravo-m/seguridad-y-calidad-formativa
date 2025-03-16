package com.example.formativa.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "profiles")
public class Profile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "avatar_uri")
    private String avatarUri;
    
    @Column(name = "favorite_games", columnDefinition = "TEXT")
    private String favoriteGames;
    
    @Column(name = "email_notifications")
    private boolean emailNotifications = true;
    
    @Column(name = "push_notifications")
    private boolean pushNotifications = true;
    
    public Profile() {
    }
    
    public Profile(User user) {
        this.user = user;
        if (user.getProfile() != this) {
            user.setProfile(this);
        }
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
        if (user != null && user.getProfile() != this) {
            user.setProfile(this);
        }
    }
    
    public String getAvatarUri() {
        return avatarUri;
    }
    
    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
    
    public String getFavoriteGames() {
        return favoriteGames;
    }
    
    public void setFavoriteGames(String favoriteGames) {
        this.favoriteGames = favoriteGames;
    }
    
    public boolean isEmailNotifications() {
        return emailNotifications;
    }
    
    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }
    
    public boolean isPushNotifications() {
        return pushNotifications;
    }
    
    public void setPushNotifications(boolean pushNotifications) {
        this.pushNotifications = pushNotifications;
    }
} 