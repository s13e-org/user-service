package com.se.user_service.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.se.user_service.dto.LoginRequestDTO;
import com.se.user_service.dto.SignupRequestDTO;
import com.se.user_service.dto.TokenRefreshRequest;
import com.se.user_service.dto.TokenResponse;
import com.se.user_service.exception.TokenRefreshException;
import com.se.user_service.helper.JwtUtils;
import com.se.user_service.model.RefreshToken;
import com.se.user_service.model.Roles;
import com.se.user_service.model.UserDetailsImpl;
import com.se.user_service.model.Users;
import com.se.user_service.repository.RoleRepository;
import com.se.user_service.repository.UserRepository;
import com.se.user_service.response.BaseResponse;
import com.se.user_service.response.Message;
import com.se.user_service.service.AuthService;
import com.se.user_service.service.RefreshTokenService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
  AuthenticationManager authenticationManager;
  UserRepository userRepository;
  RoleRepository roleRepository;
  PasswordEncoder encoder;
  JwtUtils jwtUtils;
  RefreshTokenService refreshTokenService;
  private AuthService authService;

  public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
      RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils, AuthService authService,
      RefreshTokenService refreshTokenService) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.encoder = encoder;
    this.jwtUtils = jwtUtils;
    this.authService = authService;
    this.refreshTokenService = refreshTokenService;
  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
    TokenResponse data = new TokenResponse(jwt, refreshToken.getToken());
    BaseResponse<TokenResponse> response = new BaseResponse<>();
    response.setData(data);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .flatMap(token -> userRepository.findById(token.getUserId()))
        .map(user -> {
          String token = jwtUtils.generateTokenFromUsername(user.getUsername());
          return ResponseEntity.ok(new TokenResponse(token, requestRefreshToken));
        })
        .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
            "Refresh token is not in database!"));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDTO signUpRequest) {
    BaseResponse<?> response = new BaseResponse<>();
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      response.setErrorCode(1);
      response.setMessage("exits username");
      return ResponseEntity.ok().body(response);
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      response.setErrorCode(1);
      response.setMessage("exits email");
      return ResponseEntity.ok().body(response);
    }

    boolean ok = authService.signUp(signUpRequest);
    if (ok) {
      response.setErrorCode(0);
      response.setMessage(Message.SUCCESS);
    }else{
      response.setErrorCode(1);
      response.setMessage(Message.FAIL);
    }
    return ResponseEntity.ok(response);
  }
}
