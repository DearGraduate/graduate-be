package com.example.graduate.domain.user.controller;

import com.example.graduate.domain.user.service.KakaoOAuthService;
import com.example.graduate.domain.user.service.UserService;
import com.example.graduate.domain.user.exception.UserErrorStatus;
import com.example.graduate.domain.user.exception.UserSuccessStatus;
import com.example.graduate.global.apiPayload.ApiResponse;
import com.example.graduate.global.apiPayload.exception.GeneralException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/kakao")
@RequiredArgsConstructor
@Tag(name = "카카오 소셜 로그인 API", description = "카카오 회원가입 및 로그인 기능을 제공합니다.")
public class KakaoOAuthController {
  private final KakaoOAuthService kakaoService;
  private final UserService userService;

  /**
   * 카카오 인가 코드로 사용자 인증 및 JWT 발급 처리
   */
  @PostMapping("/login")
  public ApiResponse<Void> kakaoLogin(
      @RequestParam(value = "code", required = true) String code,
      HttpServletResponse response) {
    try {
      String token = kakaoService.getAccessToken(code);
      var userInfo = kakaoService.getUserInfo(token);

      userService.loginOrRegister(userInfo, response);
      return ApiResponse.of(UserSuccessStatus.SUCCESS_LOGIN);

    } catch (Exception e) {
      throw new GeneralException(UserErrorStatus.TOKEN_FAIL);
    }
  }
}
