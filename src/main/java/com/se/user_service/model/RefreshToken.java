package com.se.user_service.model;

import java.time.Instant;
import java.util.UUID;

public class RefreshToken {
    private UUID id;
    private String token;
    private Instant expiryDate;
    private UUID userId;

    public RefreshToken() {
    }
    // public RefreshToken(UUID id, String token, Instant expiryDate, UUID userId) {
    //     this.id = id;
    //     this.token = token;
    //     this.expiryDate = expiryDate;
    //     this.userId = userId;
    // }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Instant getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }
    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    
}
