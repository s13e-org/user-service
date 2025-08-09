package com.se.user_service.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.se.user_service.helper.AuthEntryPointJwt;
import com.se.user_service.model.UserDetailsImpl;
import com.se.user_service.model.Users;
import com.se.user_service.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Users user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    // List<GrantedAuthority> authorities = userRepository.findAuthoritiesByUserId(user.getUserId());
    List<GrantedAuthority> authorities = userRepository.findPermissionNamesByUserId(user.getUserId());
    logger.error("LOGG: ----" + authorities);
    return UserDetailsImpl.build(user, authorities);
  }
}
