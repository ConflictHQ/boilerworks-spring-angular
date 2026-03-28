package com.boilerworks.api.controller;

import com.boilerworks.api.dto.ApiResponse;
import com.boilerworks.api.dto.LoginRequest;
import com.boilerworks.api.dto.UserDto;
import com.boilerworks.api.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ApiResponse<UserDto> login(@Valid @RequestBody LoginRequest request) {
    UserDto user = authService.login(request);
    return ApiResponse.ok(user);
  }

  @GetMapping("/me")
  public ApiResponse<UserDto> me(Authentication authentication) {
    UserDto user = authService.getCurrentUser(authentication);
    if (user == null) {
      return ApiResponse.error("__all__", "Not authenticated");
    }
    return ApiResponse.ok(user);
  }

  @PostMapping("/logout")
  public ApiResponse<Void> logout(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    return ApiResponse.ok(null);
  }
}
