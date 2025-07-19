package com.example.graduate.domain.user.controller;

import com.example.graduate.domain.user.dto.reponse.TokenResponse;
import com.example.graduate.domain.user.dto.request.KakaoLoginRequest;
import com.example.graduate.domain.user.service.KakaoOAuthService;
import com.example.graduate.domain.user.service.UserService;
import com.example.graduate.global.apiPayload.code.status.ErrorStatus;
import com.example.graduate.global.apiPayload.exception.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {
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
  public ResponseEntity<TokenResponse> callback(
      @Parameter(description = "카카오 인가 코드", required = true)
      @RequestParam("code") String code
  ) {
    try {
      String token = kakaoService.getAccessToken(code);
      var userInfo = kakaoService.getUserInfo(token);
      TokenResponse tokens = userService.loginOrRegister(userInfo);
      return ResponseEntity.ok(tokens);

    } catch (Exception e) {
      throw new GeneralException(ErrorStatus.TOKEN_FAIL);
    }
  }
}
