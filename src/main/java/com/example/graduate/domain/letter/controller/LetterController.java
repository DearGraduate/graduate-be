package com.example.graduate.domain.letter.controller;

import com.example.graduate.domain.letter.dto.LetterCreateRequestDTO;
import com.example.graduate.domain.letter.dto.LetterListResponseDTO;
import com.example.graduate.domain.letter.dto.LetterUpdateRequestDTO;
import com.example.graduate.domain.letter.service.LetterService;
import com.example.graduate.global.apiPayload.ApiResponse;
import com.example.graduate.domain.letter.domain.letterStatus.LetterErrorStatus;
import com.example.graduate.domain.letter.domain.letterStatus.LetterSuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Tag(name = "Letter", description = "축하글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated //추가
public class LetterController {

    private final LetterService letterService;

    // 축하글 생성
    @Operation(summary = "축하글 생성", description = "앨범 ID를 기반으로 축하글을 생성")
    @PostMapping(value = "/albums/{albumId}/letter", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createLetter(
            @PathVariable Long albumId,
            @RequestPart("data") @Valid LetterCreateRequestDTO requestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            letterService.createLetter(albumId, requestDTO, file);
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
    @Operation(summary = "축하글 수정", description = "축하글 ID를 통해 내용 수정")
    @PatchMapping(value = "/letters/{letterId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> updateLetter(
            @PathVariable Long letterId,
            @RequestPart("data") @Valid LetterUpdateRequestDTO requestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            letterService.updateLetter(letterId, requestDTO, file);
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
    @Operation(summary = "축하글 삭제", description = "축하글 ID를 통해 축하글 삭제")
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

    //축하글 전체 가져오기
    @Operation(summary = "축하글 목록 조회", description = "updatedAt와 createdAt을 사용해 목록 조회")
    @GetMapping("/letters")
    public ResponseEntity<ApiResponse<?>> getLetters(
            @RequestParam String limit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastUpdatedAt,
            @RequestParam(required = false) Long lastLetterId
    ) {
        LetterListResponseDTO result = letterService.getLetters(limit, lastUpdatedAt, lastLetterId);
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }


    //특정 앨범에 대한 축하글 가져오기
    @Operation(summary = "특정 앨범의 축하글 전체 조회", description = "특정 앨범 ID에 해당하는 축하글을 최신순으로 조회합니다.")
    @GetMapping("/albums/{albumId}/letters")
    public ResponseEntity<ApiResponse<?>> getLettersByAlbum(
            @PathVariable Long albumId,
            @RequestParam String limit
    ) {
        LetterListResponseDTO result = letterService.getLettersByAlbum(albumId, limit);
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }


}
