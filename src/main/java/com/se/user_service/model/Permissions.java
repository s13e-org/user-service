package com.se.user_service.model;

import java.util.UUID;

public class Permissions {
    private UUID permissionId;
    private String permissionName;
    private String description;
    
    public Permissions(UUID permissionId, String permissionName, String description) {
        this.permissionId = permissionId;
        this.permissionName = permissionName;
        this.description = description;
    }
    public UUID getPermissionId() {
        return permissionId;
    }
    public void setPermissionId(UUID permissionId) {
        this.permissionId = permissionId;
    }
    public String getPermissionName() {
        return permissionName;
    }
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    
}
