package com.example.graduate.domain.user.service;

import com.example.graduate.domain.user.dto.reponse.TokenResponse;
import com.example.graduate.domain.user.dto.request.KakaoUserInfo;
import com.example.graduate.domain.user.entity.User;
import com.example.graduate.domain.user.mapper.UserMapper;
import com.example.graduate.domain.user.repository.UserRepository;
import com.example.graduate.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.graduate.global.redis.RedisUtil;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final JwtUtil jwtUtil;
  private final RedisUtil redisUtil;


  public TokenResponse loginOrRegister(KakaoUserInfo info) {
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
    // 리프레시 토큰을 Redis에 저장 (key: RT:{socialId}, TTL: 초 단위)
    redisUtil.setData(
        "RT:" + user.getSocialId(),
        refresh,
        refreshExpirationMs / 1000
    );
    return new TokenResponse(access, refresh);
  }
}