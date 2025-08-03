package com.se.user_service.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.se.user_service.model.Users;
import com.se.user_service.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Cacheable(key = "#id", value = "user")
    public Users findById (UUID id){
        return userRepository.findById(id).orElse(null);
    }
    
    
}
