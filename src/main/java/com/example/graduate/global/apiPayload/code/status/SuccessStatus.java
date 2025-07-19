package com.example.graduate.global.apiPayload.code.status;

import com.example.graduate.global.apiPayload.code.BaseCode;
import com.example.graduate.global.apiPayload.dto.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

  SUCCESS_DELETE_USER("USER2003", "사용자 삭제 성공", HttpStatus.OK);

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
