package com.se.user_service.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.se.user_service.dto.SignupRequestDTO;
import com.se.user_service.model.Roles;
import com.se.user_service.model.Users;
import com.se.user_service.repository.RoleRepository;
import com.se.user_service.repository.UserRepository;

@Service
public class AuthService {
    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private PasswordEncoder encoder;

    public AuthService(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.encoder = passwordEncoder;
    }

    public boolean signUp(SignupRequestDTO signUpRequest) {
        String hashedPassword = encoder.encode(signUpRequest.getPassword());
        boolean userI = userRepo.insert(signUpRequest, hashedPassword);
        if (userI) {
            Users user = userRepo.findByUsername(signUpRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("Error: User is not found."));
            UUID userId = user.getUserId();

            String strRoles = signUpRequest.getRoleName();
            UUID roleId = null;
            if (strRoles == null) {
                Roles userRole = roleRepo.findByName("USER")
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roleId = userRole.getRoleId();
            } else {
                Roles userRole = roleRepo.findByName(strRoles)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roleId = userRole.getRoleId();
            }

            boolean role = roleRepo.insertRoleToUser(userId, roleId);
            return role;
        }
        return userI;
    }

}
