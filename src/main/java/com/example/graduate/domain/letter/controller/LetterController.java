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

    //축하글 생성
    @Operation(summary = "축하글 생성", description = "앨범 ID를 기반으로 축하글을 생성")
    @PostMapping(value = "/albums/{albumId}/letter", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createLetter(
            @PathVariable Long albumId,
            @RequestPart("data") @Valid LetterCreateRequestDTO requestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        //성공적으로 생성했을 때는 http 헤더에 무조건 200
        letterService.createLetter(albumId, requestDTO, file);
        return ApiResponse.of(LetterSuccessStatus.CREATED);

    }

    //축하글 수정
    @Operation(summary = "축하글 수정", description = "축하글 ID를 통해 내용 수정")
    @PatchMapping(value = "/letters/{letterId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> updateLetter(@PathVariable Long letterId, @RequestPart("data") @Valid LetterUpdateRequestDTO requestDTO,
                                       @RequestPart(value = "file", required = false) MultipartFile file
    ){
        letterService.updateLetter(letterId, requestDTO, file);
        return ApiResponse.of(LetterSuccessStatus.UPDATED);
    }


    //축하글 삭제
    @Operation(summary = "축하글 삭제", description = "축하글 ID를 통해 축하글 삭제")
    @DeleteMapping("/letters/{letterId}")
    public ApiResponse<?> deleteLetter(@PathVariable Long letterId){
        letterService.deleteLetter(letterId);
        return ApiResponse.of(LetterSuccessStatus.DELETED);
    }


    //축하글 전체 가져오기(프론트와 연결하지 않을 예정)
    @Operation(summary = "축하글 목록 조회", description = "updatedAt와 createdAt을 사용해 목록 조회")
    @GetMapping("/letters")
    public ApiResponse<?> getLetters(
            @RequestParam String limit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastUpdatedAt,
            @RequestParam(required = false) Long lastLetterId
    ) {
        LetterListResponseDTO result = letterService.getLetters(limit, lastUpdatedAt, lastLetterId);
        return ApiResponse.of(LetterSuccessStatus.READ_SUCCESS, result);
    }


    //특정 앨범에 대한 축하글 가져오기
    @Operation(summary = "특정 앨범의 축하글 전체 조회", description = "특정 앨범 ID에 해당하는 축하글을 최신순으로 조회합니다.")
    @GetMapping("/albums/{albumId}/letters")
    public ApiResponse<?> getLettersByAlbum(
            @PathVariable Long albumId,
            @RequestParam String limit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastUpdatedAt,
            @RequestParam(required = false) Long lastLetterId
    ) {
        LetterListResponseDTO result = letterService.getLettersByAlbum(albumId, limit, lastUpdatedAt, lastLetterId);
        return ApiResponse.of(LetterSuccessStatus.READ_SUCCESS, result);
    }


    //홈화면 특정 앨범 축하글 가져오기
    @Operation(summary = "홈화면에서 특정 앨범의 축하글 전체 조회", description = "홈화면에서 특정 앨범 ID에 해당하는 축하글을 최신순으로 조회합니다.")
    @GetMapping("/home/albums/{albumId}/letters")
    public ApiResponse<?> getHomeLettersByAlbum(
            @PathVariable Long albumId,
            @RequestParam String limit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastUpdatedAt,
            @RequestParam(required = false) Long lastLetterId
    ) {
        LetterListResponseDTO result = letterService.getLettersByAlbum(albumId, limit, lastUpdatedAt, lastLetterId);
        return ApiResponse.of(LetterSuccessStatus.READ_SUCCESS, result);
    }


}
