package com.se.user_service.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Users {
    private UUID userId;
    private String username;
    private String email;
    private String passwordHash;
    private String fullName;
    private String avatarUrl;
    private String phoneNumber;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // private List<Roles> roles;

    
    public Users(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }
    public Users() {
    }
    public Users(UUID userId, String username, String email, String passwordHash, String fullName, String avatarUrl,
            String phoneNumber, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public LocalDateTime getCreateAt() {
        return createdAt;
    }
    public void setCreateAt(LocalDateTime createAt) {
        this.createdAt = createAt;
    }
    public LocalDateTime getUpdateAt() {
        return updatedAt;
    }
    public void setUpdateAt(LocalDateTime updateAt) {
        this.updatedAt = updateAt;
    }
    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    // public List<Roles> getRoles() {
    //     return roles;
    // }
    // public void setRoles(List<Roles> roles) {
    //     this.roles = roles;
    // }

}
