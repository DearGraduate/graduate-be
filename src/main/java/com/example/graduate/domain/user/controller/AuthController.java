package com.example.graduate.domain.user.controller;

import com.example.graduate.domain.user.exception.UserSuccessStatus;
import com.example.graduate.domain.user.service.UserService;
import com.example.graduate.global.apiPayload.ApiResponse;
import com.example.graduate.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "사용자 인증 관련 API", description = "JWT 인증, 재발급, 로그아웃, 탈퇴 관련 기능을 제공합니다.")
public class AuthController {

  private final UserService userService;

  @Operation(
      summary = "Access 토큰 재발급",
      description = "Refresh 토큰을 통해 새로운 Access 토큰을 발급합니다.")
  @PostMapping("/refresh")
  public ApiResponse<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) {
    userService.reissueToken(request, response);

    return ApiResponse.of(UserSuccessStatus.SUCCESS_REISSUE);
  }

  @Operation(
      summary = "로그아웃",
      description = "Refresh 토큰을 만료시켜 로그아웃 처리합니다.")
  @PostMapping("/logout")
  public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    userService.logout(request, response);

    return ApiResponse.of(UserSuccessStatus.SUCCESS_LOGOUT);
  }

  @Operation(
      summary = "회원 탈퇴",
      description = "회원 탈퇴 처리 후 토큰 및 쿠키를 삭제합니다.")
  @PostMapping("/delete")
  public ApiResponse<Void> delete(HttpServletRequest request, HttpServletResponse response) {
    userService.delete(request, response);

    return ApiResponse.of(UserSuccessStatus.SUCCESS_DELETE_USER);
  }
}
