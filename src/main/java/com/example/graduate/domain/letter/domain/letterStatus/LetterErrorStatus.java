package com.example.graduate.domain.letter.domain.letterStatus;

import com.example.graduate.global.apiPayload.code.BaseErrorCode;
import com.example.graduate.global.apiPayload.dto.ErrorReasonDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LetterErrorStatus implements BaseErrorCode {

    // 예시 에러 코드들(해당 축하글 없음, 앨범 없음, 로그인 필요함, 서버 오류, 사용자 에러, 요청 형식 에러,,이거면 되나)
    LETTER_NOT_FOUND(HttpStatus.NOT_FOUND, "LETTER404", "해당 축하글을 찾을 수 없습니다."),
    ALBUM_NOT_FOUND(HttpStatus.NOT_FOUND, "ALBUM404", "해당 앨범을 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404", "사용자를 찾을 수 없습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "LETTER401", "축하글에 접근할 권한이 없습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "LETTER400", "요청 형식이 올바르지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "LETTER500", "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    NOT_OWNER_OF_LETTER(HttpStatus.FORBIDDEN, "LETTER403", "해당 축하글의 작성자가 아닙니다."),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "LETTER400", "지원하지 않는 이미지 확장자입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    LetterErrorStatus(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

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
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .isSuccess(false)
                .build();
    }
}
