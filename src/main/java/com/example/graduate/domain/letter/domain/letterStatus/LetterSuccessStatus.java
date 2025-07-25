package com.example.graduate.domain.letter.domain.letterStatus;

import com.example.graduate.global.apiPayload.code.BaseCode;
import com.example.graduate.global.apiPayload.dto.ReasonDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LetterSuccessStatus implements BaseCode{

    //_OK(HttpStatus.OK, "COMMON200", "성공입니다."); 예시
    CREATED(HttpStatus.CREATED, "LETTER201", "축하글이 생성되었습니다."),
    UPDATED(HttpStatus.OK, "LETTER202", "축하글이 수정되었습니다."),
    DELETED(HttpStatus.OK, "DELETE202", "축하글이 삭제되었습니다."),
    READ_SUCCESS(HttpStatus.OK, "LETTER200", "축하글 목록 조회 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    LetterSuccessStatus(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public ReasonDTO getReason(){
        return ReasonDTO.builder()
                .code(code)
                .message(message)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus(){
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
