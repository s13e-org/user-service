package com.se.user_service.model;

import java.util.UUID;

public class PermissionEndpoints {
    private UUID id;
    private UUID permissionId;
    private String httpMethod;
    private String endpointPath;
    private String service;

    public PermissionEndpoints() {
    }
    
    public PermissionEndpoints(UUID id, UUID permissionId, String httpMethod, String endpontPath, String service) {
        this.id = id;
        this.permissionId = permissionId;
        this.httpMethod = httpMethod;
        this.endpointPath = endpontPath;
        this.service = service;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public UUID getPermissionId() {
        return permissionId;
    }
    public void setPermissionId(UUID permissionId) {
        this.permissionId = permissionId;
    }
    public String getHttpMethod() {
        return httpMethod;
    }
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getEndpointPath() {
        return endpointPath;
    }

    public void setEndpointPath(String endpointPath) {
        this.endpointPath = endpointPath;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

}
