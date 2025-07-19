package com.example.graduate.global.apiPayload.code.status;

import com.example.graduate.global.apiPayload.code.BaseErrorCode;
import com.example.graduate.global.apiPayload.dto.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

  // USER 관련 에러
  USER_NOT_FOUND("USER4001", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),

  // AUTH 관련 에러

  ;

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;

  @Override
  public ErrorReasonDTO getReason() {
    return ErrorReasonDTO.builder()
        .message(message)
        .code(code)
        .isSuccess(false)
        .build();
  }

  @Override
  public ErrorReasonDTO getReasonHttpStatus() {
    return ErrorReasonDTO.builder()
        .message(message)
        .code(code)
        .isSuccess(false)
        .httpStatus(httpStatus)
        .build();
  }
}
