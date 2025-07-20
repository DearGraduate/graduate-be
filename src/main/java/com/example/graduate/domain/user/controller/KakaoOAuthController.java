package com.example.graduate.domain.user.controller;

import com.example.graduate.domain.user.service.KakaoOAuthService;
import com.example.graduate.domain.user.service.UserService;
import com.example.graduate.domain.user.exception.UserErrorStatus;
import com.example.graduate.domain.user.exception.UserSuccessStatus;
import com.example.graduate.global.apiPayload.ApiResponse;
import com.example.graduate.global.apiPayload.exception.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Null;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/kakao")
@RequiredArgsConstructor
public class KakaoOAuthController {
  private final KakaoOAuthService kakaoService;
  private final UserService userService;

  /**
   * 카카오 로그인 페이지로 리다이렉트
   */
  @Operation(
      summary = "카카오 로그인 페이지 리다이렉트",
      description = "카카오 로그인 페이지로 리다이렉트합니다.")
  @GetMapping
  public void redirectToKakao(HttpServletResponse response) throws IOException {
    response.sendRedirect(kakaoService.getAuthorizationUrl());
  }

  /**
   * 카카오 OAuth Callback 처리
   */
  @Operation(
      summary = "카카오 로그인 콜백",
      description = "인가 코드를 기반으로 카카오 사용자 정보를 받아 JWT 토큰을 발급합니다.")
  @GetMapping("/callback")
  public ApiResponse<Null> callback(
      @Parameter(description = "카카오 인가 코드", required = true)
      @RequestParam("code") String code,
      HttpServletResponse response) {
    try {
      String token = kakaoService.getAccessToken(code);
      var userInfo = kakaoService.getUserInfo(token);

      // accessToken은 응답 헤더로, refreshToken은 HttpOnly 쿠키로 전송
      userService.loginOrRegister(userInfo, response);

      return ApiResponse.of(UserSuccessStatus.SUCCESS_LOGIN);

    } catch (Exception e) {
      throw new GeneralException(UserErrorStatus.TOKEN_FAIL);
    }
  }
}
