package com.example.graduate.domain.letter.controller;

import com.example.graduate.domain.letter.dto.LetterCreateRequestDTO;
import com.example.graduate.domain.letter.dto.LetterResponseDTO;
import com.example.graduate.domain.letter.dto.LetterUpdateRequestDTO;
import com.example.graduate.domain.letter.service.LetterService;
import com.example.graduate.global.apiPayload.ApiResponse;
import com.example.graduate.global.apiPayload.dto.ErrorReasonDTO;
import com.example.graduate.global.apiPayload.dto.ReasonDTO;
import com.example.graduate.global.apiPayload.status.letter.LetterErrorStatus;
import com.example.graduate.global.apiPayload.status.letter.LetterSuccessStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated //추가
public class LetterController {

    private final LetterService letterService;

    // 축하글 생성
    @PostMapping("/albums/{albumId}/letter")
    public ResponseEntity<ApiResponse<?>> createLetter(
            @PathVariable Long albumId,
            @RequestBody @Valid LetterCreateRequestDTO requestDTO
    ) {
        try {
            letterService.createLetter(albumId, requestDTO);
            return ResponseEntity
                    .status(LetterSuccessStatus.CREATED.getHttpStatus())
                    .body(ApiResponse.of(LetterSuccessStatus.CREATED));
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(LetterErrorStatus.ALBUM_NOT_FOUND.getHttpStatus())
                    .body(ApiResponse.onFailure(
                            LetterErrorStatus.ALBUM_NOT_FOUND.getCode(),
                            LetterErrorStatus.ALBUM_NOT_FOUND.getMessage(),
                            null
                    ));
        }
    }

    //축하글 수정
    @PatchMapping("/letters/{letterId}")
    public ResponseEntity<ApiResponse<?>> updateLetter(
            @PathVariable Long letterId,
            @RequestBody @Valid LetterUpdateRequestDTO requestDTO
    ) {
        try {
            letterService.updateLetter(letterId, requestDTO);
            return ResponseEntity
                    .ok(ApiResponse.of(LetterSuccessStatus.UPDATED));
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(LetterErrorStatus.LETTER_NOT_FOUND.getHttpStatus())
                    .body(ApiResponse.onFailure(
                            LetterErrorStatus.LETTER_NOT_FOUND.getCode(),
                            LetterErrorStatus.LETTER_NOT_FOUND.getMessage(),
                            null
                    ));
        }
    }

    //축하글 삭제
    @DeleteMapping("/letters/{letterId}")
    public ResponseEntity<ApiResponse<?>> deleteLetter(@PathVariable Long letterId) {
        try {
            letterService.deleteLetter(letterId);
            return ResponseEntity
                    .ok(ApiResponse.of(LetterSuccessStatus.DELETED));
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(LetterErrorStatus.LETTER_NOT_FOUND.getHttpStatus())
                    .body(ApiResponse.onFailure(
                            LetterErrorStatus.LETTER_NOT_FOUND.getCode(),
                            LetterErrorStatus.LETTER_NOT_FOUND.getMessage(),
                            null
                    ));
        }
    }

    //축하글 가져오기
    @GetMapping("/letters")
    public ResponseEntity<ApiResponse<?>> getLetters(
            @RequestParam String limit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastCreatedAt
    ) {
        List<LetterResponseDTO> result = letterService.getLetters(limit, lastCreatedAt);
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }

}
