package com.example.graduate.domain.user.mapper;

import com.example.graduate.domain.user.dto.reponse.LoginResponseDTO;
import com.example.graduate.domain.user.entity.User;
import com.example.graduate.domain.user.dto.request.KakaoUserInfo;
import java.util.Optional;
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

  public LoginResponseDTO toLoginResponse(Optional<Long> albumIdOpt) {
    return LoginResponseDTO.builder()
        .albumExists(albumIdOpt.isPresent())
        .albumId(albumIdOpt.orElse(null))
        .build();
  }
}