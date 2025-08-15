package com.example.graduate.domain.album.service;

import com.example.graduate.domain.album.domain.Album;
import com.example.graduate.domain.album.dto.AlbumRequestDTO;
import com.example.graduate.domain.album.dto.AlbumResponseDTO;
import com.example.graduate.domain.album.repository.AlbumRepository;
import com.example.graduate.domain.letter.repository.LetterRepository;
import com.example.graduate.domain.user.entity.User;
import com.example.graduate.domain.user.repository.UserRepository;
import com.example.graduate.global.SecurityUtil;
import com.example.graduate.global.apiPayload.exception.GeneralException;
import com.example.graduate.domain.album.status.AlbumErrorStatus;
import com.example.graduate.global.redis.RedisUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;
    private final LetterRepository letterRepository;
    private final SecurityUtil securityUtil;
    private final RedisUtil redisUtil;

    private Long getCurrentUserId() {
        User user = securityUtil.getCurrentUser();
        return user.getId();
    }
    
    @Transactional
    public AlbumResponseDTO createAlbum(AlbumRequestDTO dto) {
        Long userId = getCurrentUserId();

        if (albumRepository.existsByUserId(userId)) {
            throw new GeneralException(AlbumErrorStatus._ALBUM_ALREADY_EXISTS);
        }

        // 유효성 검사용 (memberId 존재 여부 확인)
        userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(AlbumErrorStatus._MEMBER_NOT_FOUND));

        Album album = Album.builder()
                .userId(userId) // 💥 Long 값 바로 세팅
                .albumName(dto.getAlbumName())
                .albumType(dto.getAlbumType())
                .description(dto.getDescription())
                .graduationDate(dto.getGraduationDate())
                .build();

        albumRepository.save(album);
        redisUtil.increment("project:album:count");

        return AlbumResponseDTO.builder()
                .id(album.getId())
                .albumName(album.getAlbumName())
                .albumType(album.getAlbumType())
                .description(album.getDescription())
                .graduationDate(album.getGraduationDate())
                .createdAt(album.getCreatedAt())
                .build();
    }
    @Transactional
    public void updateAlbum(AlbumRequestDTO dto) {
        Long userId = getCurrentUserId();

        Album album = albumRepository.findByUserId(userId)
                .orElseThrow(() -> new GeneralException(AlbumErrorStatus._ALBUM_NOT_FOUND));

        if (dto.getAlbumName() == null || dto.getAlbumType() == null || dto.getGraduationDate() == null) {
            throw new GeneralException(AlbumErrorStatus._REQUIRED_FIELDS_MISSING);
        }

        album.setGraduationDate(dto.getGraduationDate());
        album.setAlbumName(dto.getAlbumName());
        album.setAlbumType(dto.getAlbumType());
        album.setDescription(dto.getDescription());
    }

    @Transactional
    public void deleteAlbum() {
        Long userId = getCurrentUserId();

        Album album = albumRepository.findByUserId(userId)
                .orElseThrow(() -> new GeneralException(AlbumErrorStatus._ALBUM_NOT_FOUND));
        // 연관된 축하글 삭제
        letterRepository.deleteAllByAlbumId(album.getId());
        albumRepository.delete(album);
    }

    @Transactional
    public void deleteAlbumIfExists() {
        Long userId = getCurrentUserId();

        albumRepository.findByUserId(userId).ifPresent(album -> {
            letterRepository.deleteAllByAlbumId(album.getId());
            albumRepository.delete(album);
        });
    }

    public AlbumResponseDTO getAlbum() {
        Long userId = getCurrentUserId();

        Album album = albumRepository.findByUserId(userId)
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