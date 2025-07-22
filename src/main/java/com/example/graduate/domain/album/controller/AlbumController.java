package com.example.graduate.domain.album.controller;

import com.example.graduate.domain.album.dto.AlbumRequestDTO;
import com.example.graduate.domain.album.dto.AlbumResponseDTO;
import com.example.graduate.domain.album.service.AlbumService;
import com.example.graduate.global.apiPayload.ApiResponse;
import com.example.graduate.domain.album.status.AlbumSuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/albums/{userId}")
@RequiredArgsConstructor
@Tag(name = "앨범 API", description = "앨범 관련 API입니다.")
public class AlbumController {

    private final AlbumService albumService;

    @Operation(summary = "앨범 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createAlbum(
            @PathVariable Long userId,
            @Valid @RequestBody AlbumRequestDTO dto) {
        AlbumResponseDTO result = albumService.createAlbum(userId, dto);
        return ResponseEntity
                .status(AlbumSuccessStatus._CREATE_ALBUM_SUCCESS.getHttpStatus())
                .body(ApiResponse.of(AlbumSuccessStatus._CREATE_ALBUM_SUCCESS, result));
    }

    @Operation(summary = "앨범 수정")
    @PatchMapping
    public ResponseEntity<ApiResponse<?>> updateAlbum (
            @PathVariable Long userId,
            @Valid @RequestBody AlbumRequestDTO dto){
        albumService.updateAlbum(userId, dto);
        return ResponseEntity
                .status(AlbumSuccessStatus._UPDATE_ALBUM_SUCCESS.getHttpStatus())
                .body(ApiResponse.of(AlbumSuccessStatus._UPDATE_ALBUM_SUCCESS));
    }


    @Operation(summary = "앨범 삭제")
    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> deleteAlbum(@PathVariable Long userId) {
        albumService.deleteAlbum(userId);
        return ResponseEntity
                .status(AlbumSuccessStatus._DELETE_ALBUM_SUCCESS.getHttpStatus())
                .body(ApiResponse.of(AlbumSuccessStatus._DELETE_ALBUM_SUCCESS));
    }

    @Operation(summary = "앨범 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<AlbumResponseDTO>> getAlbum(
            @PathVariable Long userId) {
        AlbumResponseDTO album = albumService.getAlbum(userId);
        return ResponseEntity
                .status(AlbumSuccessStatus._GET_ALBUM_SUCCESS.getHttpStatus())
                .body(ApiResponse.of(AlbumSuccessStatus._GET_ALBUM_SUCCESS, album));
    }


}
