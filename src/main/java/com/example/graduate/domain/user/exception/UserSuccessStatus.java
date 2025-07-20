package com.example.graduate.domain.user.exception;

import com.example.graduate.global.apiPayload.code.BaseCode;
import com.example.graduate.global.apiPayload.dto.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserSuccessStatus implements BaseCode {

  SUCCESS_DELETE_USER("USER2000", "사용자 삭제 성공", HttpStatus.OK),

  SUCCESS_LOGIN("AUTH2005", "사용자 로그인 성공", HttpStatus.OK),

  SUCCESS_REISSUE("AUTH2010", "Access Token 재발급 성공", HttpStatus.OK),

  SUCCESS_LOGOUT("AUTH2015", "로그아웃 성공", HttpStatus.OK),

  ;

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;

  @Override
  public ReasonDTO getReason() {
    return ReasonDTO.builder()
        .isSuccess(true)
        .code(code)
        .message(message)
        .build();
  }

  @Override
  public ReasonDTO getReasonHttpStatus() {
    return ReasonDTO.builder()
        .isSuccess(true)
        .code(code)
        .message(message)
        .httpStatus(httpStatus)
        .build();
  }
}
