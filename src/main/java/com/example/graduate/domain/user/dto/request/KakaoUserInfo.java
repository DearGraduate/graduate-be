package com.example.graduate.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoUserInfo {
  private String id;
  private String nickname;
  private String email;
}