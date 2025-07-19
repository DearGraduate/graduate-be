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
  TOKEN_NOT_FOUND("AUTH4006", "리프레시 토큰이 필요합니다.", HttpStatus.FORBIDDEN),
  TOKEN_INVALID("AUTH4007", "유효하지 않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED),
  TOKEN_EXPIRED("AUTH4008", "리프레시 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
  TOKEN_FAIL("AUTH4009", "토큰 파싱 또는 추출에 실패했습니다.", HttpStatus.BAD_REQUEST),

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
