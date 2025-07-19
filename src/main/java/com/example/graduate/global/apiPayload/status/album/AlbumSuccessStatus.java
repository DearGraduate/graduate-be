package com.example.graduate.global.apiPayload.status.album;

import com.example.graduate.global.apiPayload.code.BaseCode;
import com.example.graduate.global.apiPayload.dto.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AlbumSuccessStatus implements BaseCode {

    _CREATE_ALBUM_SUCCESS(HttpStatus.CREATED, "ALBUM201", "앨범 생성 성공"),
    _UPDATE_ALBUM_SUCCESS(HttpStatus.OK, "ALBUM2001", "앨범 수정 성공"),
    _DELETE_ALBUM_SUCCESS(HttpStatus.OK, "ALBUM2002", "앨범 삭제 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .code(code)
                .message(message)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .code(code)
                .message(message)
                .httpStatus(httpStatus)
                .isSuccess(true)
                .build();
    }
}
