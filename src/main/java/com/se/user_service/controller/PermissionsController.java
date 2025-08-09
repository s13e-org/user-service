package com.se.user_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.se.user_service.dto.GetEndPointResponseDTO;
import com.se.user_service.model.PermissionEndpoints;
import com.se.user_service.response.BaseResponse;
import com.se.user_service.service.PermissionsService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/permissions")
public class PermissionsController {
    private final PermissionsService permissionsService;

    public PermissionsController(PermissionsService permissionsService) {
        this.permissionsService = permissionsService;
    }
    
    @PreAuthorize("hasAuthority('GET_PERMISSION')")
    @GetMapping("/endpoint-user")
    public ResponseEntity<?> getEndpoints(HttpServletRequest request) {
        
        Set<PermissionEndpoints> endpoints = permissionsService.getEndpoint(request);
        BaseResponse<Set<GetEndPointResponseDTO>> response = new BaseResponse<>();
        Set<GetEndPointResponseDTO> data = new HashSet<>();
        for (PermissionEndpoints permissionEndpoints : endpoints) {
            GetEndPointResponseDTO res = new GetEndPointResponseDTO(permissionEndpoints.getHttpMethod(), permissionEndpoints.getEndpointPath(), permissionEndpoints.getService());
            data.add(res);
        }
        response.setData(data);
        return ResponseEntity.ok().body(response);
    }
    
}
