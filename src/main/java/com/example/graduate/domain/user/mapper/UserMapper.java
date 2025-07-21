package com.example.graduate.domain.user.mapper;

import com.example.graduate.domain.user.entity.User;
import com.example.graduate.domain.user.dto.request.KakaoUserInfo;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public User toEntity(KakaoUserInfo info) {
    return User.builder()
        .socialId(info.getId())
        .name(info.getNickname())
        .email(info.getEmail())
        .build();
  }
}