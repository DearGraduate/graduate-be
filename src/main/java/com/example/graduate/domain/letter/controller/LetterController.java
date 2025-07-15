package com.example.graduate.domain.letter.controller;

import com.example.graduate.domain.letter.dto.LetterCreateRequestDTO;
import com.example.graduate.domain.letter.dto.LetterUpdateRequestDTO;
import com.example.graduate.domain.letter.service.LetterService;
import com.example.graduate.global.apiPayload.dto.ErrorReasonDTO;
import com.example.graduate.global.apiPayload.dto.ReasonDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LetterController {

    private final LetterService letterService;

    // 축하글 생성
    @PostMapping("/albums/{albumId}/letter")
    public ResponseEntity<?> createLetter(
            @PathVariable Long albumId,
            @RequestBody LetterCreateRequestDTO requestDTO
    ) {
        try {
            letterService.createLetter(albumId, requestDTO);

            ReasonDTO response = ReasonDTO.builder()
                    .httpStatus(HttpStatus.CREATED)
                    .isSuccess(true)
                    .code("COMMON200")
                    .message("축하 메시지가 성공적으로 등록되었습니다.")
                    .build();

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (EntityNotFoundException e) {
            ErrorReasonDTO error = ErrorReasonDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .isSuccess(false)
                    .code("LETTER404")
                    .message("앨범을 찾을 수 없습니다.")
                    .build();

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    //축하글 수정
    @PatchMapping("/letters/{letterId}")
    public ResponseEntity<?> updateLetter(
            @PathVariable Long letterId,@RequestBody LetterUpdateRequestDTO requestDTO
    ){
        try{
            letterService.updateLetter(letterId, requestDTO);

            ReasonDTO response = ReasonDTO.builder()
                    .httpStatus(HttpStatus.OK)
                    .isSuccess(true)
                    .code("COMMON200")
                    .message("성공적으로 수정되었습니다.")
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch(EntityNotFoundException e){
            ErrorReasonDTO error = ErrorReasonDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .isSuccess(false)
                    .code("404")
                    .message(e.getMessage())
                    .build();

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    //축하글 삭제
    @DeleteMapping("/letters/{letterId}")
    public ResponseEntity<?> deleteLetter(@PathVariable Long letterId) {
        try {
            letterService.deleteLetter(letterId);

            ReasonDTO response = ReasonDTO.builder()
                    .httpStatus(HttpStatus.OK)
                    .isSuccess(true)
                    .code("COMMON200")
                    .message("축하 메시지가 성공적으로 삭제되었습니다.")
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            ErrorReasonDTO error = ErrorReasonDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .isSuccess(false)
                    .code("LETTER404")
                    .message(e.getMessage())
                    .build();

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    //축하글 가져오기
    //@GetMapping("/letters")
}
