package com.example.graduate.domain.user.service;

import com.example.graduate.domain.user.dto.request.KakaoUserInfo;
import com.example.graduate.domain.user.entity.User;
import com.example.graduate.domain.user.exception.UserErrorStatus;
import com.example.graduate.domain.user.mapper.UserMapper;
import com.example.graduate.domain.user.repository.UserRepository;
import com.example.graduate.global.apiPayload.exception.GeneralException;
import com.example.graduate.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import com.example.graduate.global.redis.RedisUtil;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final JwtUtil jwtUtil;
  private final RedisUtil redisUtil;

  @Value("${cookie.secure}")
  private boolean secureCookie;

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

  private ResponseCookie createCookie(String key, String value, long maxAge) {
    return ResponseCookie.from(key, value)
        .httpOnly(true)
        .secure(secureCookie)
        .path("/")
        .maxAge(maxAge)
        .build();
  }
}