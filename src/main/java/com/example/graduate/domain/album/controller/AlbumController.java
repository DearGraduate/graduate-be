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
@RequestMapping("/api/albums")
@RequiredArgsConstructor
@Tag(name = "앨범 API", description = "앨범 관련 API입니다.")
public class AlbumController {

    private final AlbumService albumService;

    @Operation(summary = "앨범 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createAlbum(
            @Valid @RequestBody AlbumRequestDTO dto) {
        AlbumResponseDTO result = albumService.createAlbum(dto);
        return ResponseEntity
                .status(AlbumSuccessStatus._CREATE_ALBUM_SUCCESS.getHttpStatus())
                .body(ApiResponse.of(AlbumSuccessStatus._CREATE_ALBUM_SUCCESS, result));
    }

    @Operation(summary = "앨범 수정")
    @PatchMapping
    public ResponseEntity<ApiResponse<?>> updateAlbum (
            @Valid @RequestBody AlbumRequestDTO dto){
        albumService.updateAlbum(dto);
        return ResponseEntity
                .status(AlbumSuccessStatus._UPDATE_ALBUM_SUCCESS.getHttpStatus())
                .body(ApiResponse.of(AlbumSuccessStatus._UPDATE_ALBUM_SUCCESS));
    }


    @Operation(summary = "앨범 삭제")
    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> deleteAlbum() {
        albumService.deleteAlbum();
        return ResponseEntity
                .status(AlbumSuccessStatus._DELETE_ALBUM_SUCCESS.getHttpStatus())
                .body(ApiResponse.of(AlbumSuccessStatus._DELETE_ALBUM_SUCCESS));
    }

    @Operation(summary = "앨범 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<AlbumResponseDTO>> getAlbum() {
        AlbumResponseDTO album = albumService.getAlbum();
        return ResponseEntity
                .status(AlbumSuccessStatus._GET_ALBUM_SUCCESS.getHttpStatus())
                .body(ApiResponse.of(AlbumSuccessStatus._GET_ALBUM_SUCCESS, album));
    }

    @Operation(summary = "앨범 ID로 조회")
    @GetMapping("/{albumId}")
    public ResponseEntity<ApiResponse<AlbumResponseDTO>> getAlbumById(@PathVariable Long albumId) {
        AlbumResponseDTO album = albumService.getAlbumById(albumId);
        return ResponseEntity
                .status(AlbumSuccessStatus._GET_ALBUM_SUCCESS.getHttpStatus())
                .body(ApiResponse.of(AlbumSuccessStatus._GET_ALBUM_SUCCESS, album));
    }

}
