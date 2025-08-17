package com.example.graduate.domain.user.controller;

import com.example.graduate.domain.user.dto.reponse.LoginResponseDTO;
import com.example.graduate.domain.user.service.KakaoOAuthService;
import com.example.graduate.domain.user.service.UserService;
import com.example.graduate.domain.user.exception.UserErrorStatus;
import com.example.graduate.domain.user.exception.UserSuccessStatus;
import com.example.graduate.global.apiPayload.ApiResponse;
import com.example.graduate.global.apiPayload.exception.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
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
  @Operation(
      summary = "회원가입 또는 로그인",
      description = "카카오 인가 코드로 회원가입 또는 로그인 처리를 하고, 사용자의 앨범 존재 유무와 앨범 id를 반환합니다.")
  @PostMapping("/login")
  public ApiResponse<LoginResponseDTO> kakaoLogin(
      @RequestParam(value = "code") String code,
      HttpServletResponse response) {
    try {
      String token = kakaoService.getAccessToken(code);
      var userInfo = kakaoService.getUserInfo(token);

      LoginResponseDTO dto = userService.loginOrRegister(userInfo, response);
      return ApiResponse.of(UserSuccessStatus.SUCCESS_LOGIN, dto);

    } catch (Exception e) {
      throw new GeneralException(UserErrorStatus.TOKEN_FAIL);
    }
  }
}
