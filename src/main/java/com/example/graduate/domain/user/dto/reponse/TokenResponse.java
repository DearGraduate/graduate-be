package com.example.graduate.domain.user.dto.reponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(title = "UserResponse DTO", description = "사용자 토큰 반환 DTO")
public class TokenResponse {

  private String accessToken;
}
