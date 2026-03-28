package com.boilerworks.api.service;

import com.boilerworks.api.dto.LoginRequest;
import com.boilerworks.api.dto.UserDto;
import com.boilerworks.api.model.AppUser;
import com.boilerworks.api.repository.AppUserRepository;
import com.boilerworks.api.security.BoilerworksUserDetails;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final AppUserRepository userRepository;

  @Transactional
  public UserDto login(LoginRequest request) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    BoilerworksUserDetails userDetails = (BoilerworksUserDetails) authentication.getPrincipal();

    AppUser user = userRepository.findById(userDetails.getUserId()).orElseThrow();
    user.setLastLogin(Instant.now());
    userRepository.save(user);

    return UserDto.from(user);
  }

  public UserDto getCurrentUser(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }
    BoilerworksUserDetails userDetails = (BoilerworksUserDetails) authentication.getPrincipal();
    AppUser user = userRepository.findById(userDetails.getUserId()).orElse(null);
    return user != null ? UserDto.from(user) : null;
  }
}
