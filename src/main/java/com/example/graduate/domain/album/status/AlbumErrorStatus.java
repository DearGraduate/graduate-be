package com.example.graduate.domain.album.status;

import com.example.graduate.global.apiPayload.code.BaseErrorCode;
import com.example.graduate.global.apiPayload.dto.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AlbumErrorStatus implements BaseErrorCode {

    // AlbumErrorStatus.java
    _REQUIRED_FIELDS_MISSING( HttpStatus.BAD_REQUEST,"ALBUM4001", "앨범 제목과 졸업일자는 반드시 입력되어야 합니다."),
    _ALBUM_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "ALBUM4001", "이미 앨범이 존재합니다."),
    _ALBUM_NOT_FOUND(HttpStatus.NOT_FOUND, "ALBUM4041", "앨범을 찾을 수 없습니다."),
    _MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "ALBUM4042", "사용자를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .code(code)
                .message(message)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .code(code)
                .message(message)
                .httpStatus(httpStatus)
                .isSuccess(false)
                .build();
    }
}