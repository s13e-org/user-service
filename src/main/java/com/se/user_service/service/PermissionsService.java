package com.se.user_service.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.se.user_service.model.PermissionEndpoints;
import com.se.user_service.repository.PermissionsRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class PermissionsService {
    private final PermissionsRepository repo;

    public PermissionsService(PermissionsRepository repo) {
        this.repo = repo;
    }
    
    public Set<String> getPermissionsByRole(String roleName){
        return repo.getPermissionsByRole(roleName);
    }

    public boolean hasPermission (String[] roles, String permission){
        return repo.hasPermission(roles, permission);
    }

    public Set<PermissionEndpoints> getEndpoint (HttpServletRequest request){
        return repo.getEndpoint(request);
    }
}
