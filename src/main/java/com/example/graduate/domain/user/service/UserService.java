package com.example.graduate.domain.user.service;

import static org.hibernate.query.sqm.tree.SqmNode.log;

import com.example.graduate.domain.album.service.AlbumService;
import com.example.graduate.domain.user.dto.request.KakaoUserInfo;
import com.example.graduate.domain.user.entity.User;
import com.example.graduate.domain.user.exception.UserErrorStatus;
import com.example.graduate.domain.user.mapper.UserMapper;
import com.example.graduate.domain.user.repository.UserRepository;
import com.example.graduate.global.SecurityUtil;
import com.example.graduate.global.apiPayload.exception.GeneralException;
import com.example.graduate.global.jwt.JwtUtil;
import com.example.graduate.global.security.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import com.example.graduate.global.redis.RedisUtil;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final AlbumService albumService;
  private final UserMapper userMapper;
  private final JwtUtil jwtUtil;
  private final RedisUtil redisUtil;
  private final SecurityUtil securityUtil;
  private final KakaoOAuthService kakaoOAuthService;

  @Value("${cookie.secure}")
  private boolean secureCookie;

  /**
   * 카카오 사용자 정보를 기반으로 로그인 또는 회원가입을 처리하고 JWT를 발급합니다. <hr>
   * AccessToken은 Authorization 헤더로, RefreshToken은 HttpOnly 쿠키로 반환됩니다.
   *
   * @param info     카카오 사용자 정보
   * @param response 클라이언트에 토큰을 전달할 HttpServletResponse
   */
  public void loginOrRegister(KakaoUserInfo info, HttpServletResponse response) {
    User user = userRepository.findBySocialId(info.getId())
        .orElseGet(() -> userRepository.save(userMapper.toEntity(info)));

    // JWT 생성: socialId, name만 포함
    String access = jwtUtil.createJwt(
        "access",
        user.getSocialId(),
        user.getName(),
        null,
        1000 * 60 * 60L // 예시: 1시간
    );

    long refreshExpirationMs = 1000L * 60 * 60 * 24 * 7; // 7일
    String refresh = jwtUtil.createJwt(
        "refresh",
        user.getSocialId(),
        user.getName(),
        null,
        refreshExpirationMs
    );
    // 리프레시 토큰을 Redis에 저장 (key: refresh:{socialId}, TTL: 초 단위)
    redisUtil.setData(
        "refresh:" + user.getSocialId(),
        refresh,
        refreshExpirationMs / 1000
    );

    // refreshToken을 HttpOnly 쿠키에 담아 클라이언트로 전송
    ResponseCookie refreshCookie = createCookie("refreshToken", refresh, refreshExpirationMs / 1000);
    response.addHeader("Set-Cookie", refreshCookie.toString());
    response.addHeader("Authorization", "Bearer " + access);
  }

  /**
   * 클라이언트의 RefreshToken 쿠키를 기반으로 새로운 AccessToken과 RefreshToken을 발급합니다.
   * 새 토큰은 Authorization 헤더 및 HttpOnly 쿠키로 반환됩니다.
   *
   * @param request  클라이언트 요청 (쿠키 포함)
   * @param response 토큰을 전달할 HttpServletResponse
   */
  public void reissueToken(HttpServletRequest request, HttpServletResponse response) {
    String refresh = null;
    jakarta.servlet.http.Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (jakarta.servlet.http.Cookie cookie : cookies) {
        if (cookie.getName().equals("refreshToken")) {
          refresh = cookie.getValue();
          break;
        }
      }
    }

    if (refresh == null) {
      throw new GeneralException(UserErrorStatus.TOKEN_NOT_FOUND);
    }

    if (jwtUtil.isExpired(refresh)) {
      throw new GeneralException(UserErrorStatus.TOKEN_EXPIRED);
    }

    String category = jwtUtil.getCategory(refresh);
    if (!"refresh".equals(category)) {
      throw new GeneralException(UserErrorStatus.TOKEN_INVALID);
    }

    String socialId = jwtUtil.getSocialId(refresh);
    String name = jwtUtil.getName(refresh);
    String role = jwtUtil.getRole(refresh);

    if (!redisUtil.existData("refresh:" + socialId)) {
      throw new GeneralException(UserErrorStatus.TOKEN_NOT_FOUND);
    }

    long accessExpiredMs = 1000 * 60 * 60; // 1시간
    long refreshExpiredMs = 1000 * 60 * 60 * 24 * 7; // 7일

    String access = jwtUtil.createJwt("access", socialId, name, role, accessExpiredMs);
    String newRefresh = jwtUtil.createJwt("refresh", socialId, name, role, refreshExpiredMs);

    redisUtil.deleteData("refresh:" + socialId);
    redisUtil.setData("refresh:" + socialId, newRefresh, refreshExpiredMs / 1000);

    ResponseCookie refreshCookie = createCookie("refreshToken", newRefresh,
        refreshExpiredMs / 1000);

    response.addHeader("Authorization", "Bearer " + access);
    response.addHeader("Set-Cookie", refreshCookie.toString());
  }

  /**
   * 주어진 정보를 기반으로 HttpOnly RefreshToken 쿠키를 생성합니다.
   *
   * @param key     쿠키 키
   * @param value   쿠키 값 (refresh token)
   * @param maxAge  쿠키 유효 기간 (초 단위)
   * @return 생성된 ResponseCookie
   */
  private ResponseCookie createCookie(String key, String value, long maxAge) {
    ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(key, value)
        .httpOnly(true)
        .secure(secureCookie)
        .path("/")
        .maxAge(maxAge);

    // prod 환경이면 SameSite + domain 설정 추가
    if (secureCookie) {
      builder.domain("photory.site");
      builder.sameSite("None");
    }

    return builder.build();
  }

  /**
   * 로그아웃 처리: Redis에서 해당 사용자의 RefreshToken을 제거합니다.
   *
   * @param request 클라이언트 요청 (쿠키에서 refreshToken을 추출)
   * @param response 클라이언트에 빈 쿠키를 전달하여 삭제 처리
   */
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    String refresh = null;
    Cookie[] cookies = request.getCookies();

    if (cookies != null) {
      for (jakarta.servlet.http.Cookie cookie : cookies) {
        if (cookie.getName().equals("refreshToken")) {
          refresh = cookie.getValue();
          break;
        }
      }
    }

    if (refresh == null) {
      throw new GeneralException(UserErrorStatus.TOKEN_NOT_FOUND);
    }

    String socialId = jwtUtil.getSocialId(refresh);
    redisUtil.deleteData("refresh:" + socialId);

    ResponseCookie expiredCookie = ResponseCookie.from("refreshToken", "")
        .maxAge(0)
        .httpOnly(true)
        .secure(secureCookie)
        .path("/")
        .build();
    response.addHeader("Set-Cookie", expiredCookie.toString());
  }

  /**
   * 회원 탈퇴 처리
   *
   * @param response 만료된 refreshToken 쿠키를 추가하기 위한 HTTP 응답 객체
   * @throws GeneralException 카카오 연결 해제에 실패(이미 해제된 경우 제외)한 경우
   */
  public void delete(HttpServletResponse response) {
    User user = securityUtil.getCurrentUser();
    String socialId = user.getSocialId();

    // 카카오 연결 해제
    try {
      kakaoOAuthService.unlinkKakao(socialId);
    } catch (HttpClientErrorException e) {
      String body = e.getResponseBodyAsString();
      int status = e.getStatusCode().value();
      log.warn("Kakao unlink failed: status={}, body={}", status, body);

      // 이미 해제/미등록 사용자 멱등 처리
      if (!(status == 400 && body != null &&
          (body.contains("NotRegisteredUser") ||
              body.contains("already") ||
              body.contains("unlinked")))) {
        throw new GeneralException(UserErrorStatus.UNLINK_FAILED);
      }
      log.info("Kakao already unlinked. Proceed.");
    }

    // 사용자 로그인 정보를 사용하여 앨범 삭제 (사용자 삭제보다 먼저 호출)
    albumService.deleteAlbumIfExists();
    userRepository.delete(user);
    redisUtil.deleteData("refresh:" + socialId);

    ResponseCookie expiredCookie = ResponseCookie.from("refreshToken", "")
        .maxAge(0)
        .httpOnly(true)
        .secure(secureCookie)
        .path("/")
        .build();
    response.addHeader("Set-Cookie", expiredCookie.toString());
  }
}