package com.se.user_service.model;

import java.util.List;
import java.util.UUID;

public class Roles {
    private UUID roleId;
    private String roleName;
    private String description;
    private List<Permissions> permissions;

    public UUID getRoleId() {
        return roleId;
    }
    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<Permissions> getPermissions() {
        return permissions;
    }
    public void setPermissions(List<Permissions> permissions) {
        this.permissions = permissions;
    }
    
}
