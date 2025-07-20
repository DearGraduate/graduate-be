package com.example.graduate.domain.album.service;

import com.example.graduate.domain.album.domain.Album;
import com.example.graduate.domain.album.dto.AlbumRequestDTO;
import com.example.graduate.domain.album.dto.AlbumResponseDTO;
import com.example.graduate.domain.album.repository.AlbumRepository;
import com.example.graduate.domain.member.repository.MemberRepository;
import com.example.graduate.global.apiPayload.exception.GeneralException;
import com.example.graduate.global.apiPayload.status.album.AlbumErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final MemberRepository memberRepository;

    public AlbumResponseDTO createAlbum(Long userId, AlbumRequestDTO dto) {
        if (albumRepository.existsByuserId(userId)) {
            throw new GeneralException(AlbumErrorStatus._ALBUM_ALREADY_EXISTS);
        }

        // 유효성 검사용 (memberId 존재 여부 확인)
        memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(AlbumErrorStatus._MEMBER_NOT_FOUND));

        Album album = Album.builder()
                .userId(userId) // 💥 Long 값 바로 세팅
                .albumName(dto.getAlbumName())
                .description(dto.getDescription())
                .graduationDate(dto.getGraduationDate())
                .build();

        albumRepository.save(album);
        return AlbumResponseDTO.builder()
                .id(album.getId())
                .albumName(album.getAlbumName())
                .description(album.getDescription())
                .graduationDate(album.getGraduationDate())
                .createdAt(album.getCreatedAt())
                .build();
    }
    @Transactional
    public void updateAlbum(Long userId, AlbumRequestDTO dto) {
        Album album = albumRepository.findByuserId(userId)
                .orElseThrow(() -> new GeneralException(AlbumErrorStatus._ALBUM_NOT_FOUND));

        album.setAlbumName(dto.getAlbumName());
        album.setDescription(dto.getDescription());
        album.setGraduationDate(dto.getGraduationDate());
    }

    public void deleteAlbum(Long userId) {
        Album album = albumRepository.findByuserId(userId)
                .orElseThrow(() -> new GeneralException(AlbumErrorStatus._ALBUM_NOT_FOUND));
        albumRepository.delete(album);
    }

    public AlbumResponseDTO getAlbum(Long userId) {
        Album album = albumRepository.findByuserId(userId)
                .orElseThrow(() -> new GeneralException(AlbumErrorStatus._ALBUM_NOT_FOUND));

        return AlbumResponseDTO.builder()
                .id(album.getId())
                .albumName(album.getAlbumName())
                .description(album.getDescription())
                .graduationDate(album.getGraduationDate())
                .createdAt(album.getCreatedAt())
                .build();
    }

}