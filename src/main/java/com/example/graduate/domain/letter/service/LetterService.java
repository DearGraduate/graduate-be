package com.example.graduate.domain.letter.service;

import com.example.graduate.domain.album.repository.AlbumRepository;
import com.example.graduate.domain.letter.domain.Letter;
import com.example.graduate.domain.letter.dto.LetterCreateRequestDTO;
import com.example.graduate.domain.letter.dto.LetterListResponseDTO;
import com.example.graduate.domain.letter.dto.LetterResponseDTO;
import com.example.graduate.domain.letter.dto.LetterUpdateRequestDTO;
import com.example.graduate.domain.letter.repository.LetterRepository;
import com.example.graduate.global.SecurityUtil;
import com.example.graduate.global.apiPayload.exception.GeneralException;
import com.example.graduate.domain.letter.domain.letterStatus.LetterErrorStatus;
import com.example.graduate.global.aws.s3.AmazonS3Manager;
import com.example.graduate.global.aws.s3.Uuid;
import com.example.graduate.global.aws.s3.UuidRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;
    private final AlbumRepository albumRepository;
    private final SecurityUtil securityUtil;

    //s3 관련 코드 추가
    private final AmazonS3Manager amazonS3Manager;
    private final UuidRepository uuidRepository;

    private Long getCurrentUserId() {
        return securityUtil.getCurrentUser().getId();
    }

    @Transactional
    public void createLetter(Long albumId, LetterCreateRequestDTO requestDTO, MultipartFile file){
        albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(LetterErrorStatus.ALBUM_NOT_FOUND));

        String picUrl = null;

        // 파일이 있으면 UUID 생성 + S3 업로드 + URL 추출
        if (file != null && !file.isEmpty()) {
            Uuid savedUuid = uuidRepository.save(
                    Uuid.builder()
                            .uuid(UUID.randomUUID().toString())
                            .build()
            );

            String keyName = amazonS3Manager.generateLetterKeyName(savedUuid);
            picUrl = amazonS3Manager.uploadFile(keyName, file);
        }

        //추가
        Long userId = getCurrentUserId();

        Letter letter = Letter.builder()
                .writerName(requestDTO.getWriterName())
                .message(requestDTO.getMessage())
                .isPublic(requestDTO.getIsPublic())
                .albumId(albumId)
                .picUrl(picUrl)
                .userId(userId)
                .build();
        letterRepository.save(letter);
    }

    //축하글 수정
    @Transactional
    public void updateLetter(Long letterId, LetterUpdateRequestDTO requestDTO, MultipartFile file) {
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new GeneralException(LetterErrorStatus.LETTER_NOT_FOUND));

        if (!letter.getUserId().equals(getCurrentUserId())) {
            throw new GeneralException(LetterErrorStatus.NOT_OWNER_OF_LETTER);
        }

        // 이미지 파일이 새로 들어온 경우 기존 S3 이미지 삭제 후 새로 업로드
        if (file != null && !file.isEmpty()) {
            // 기존 이미지가 있으면 삭제
            if (letter.getPicUrl() != null) {
                amazonS3Manager.deleteFileByUrl(letter.getPicUrl());
            }

            // 새 이미지 업로드
            Uuid savedUuid = uuidRepository.save(
                    Uuid.builder()
                            .uuid(UUID.randomUUID().toString())
                            .build()
            );
            String keyName = amazonS3Manager.generateLetterKeyName(savedUuid);
            String picUrl = amazonS3Manager.uploadFile(keyName, file);
            letter.setPicUrl(picUrl);
        }

        if (requestDTO.getWriterName() != null) {
            letter.setWriterName(requestDTO.getWriterName());
        }
        if (requestDTO.getMessage() != null) {
            letter.setMessage(requestDTO.getMessage());
        }
        if (requestDTO.getIsPublic() != null) {
            letter.setIsPublic(requestDTO.getIsPublic());
        }

        letterRepository.save(letter);
    }


    //축하글 삭제
    @Transactional
    public void deleteLetter(Long letterId) {
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new GeneralException(LetterErrorStatus.LETTER_NOT_FOUND));

        if (!letter.getUserId().equals(getCurrentUserId())) {
            throw new GeneralException(LetterErrorStatus.NOT_OWNER_OF_LETTER);
        }

        letterRepository.delete(letter);
    }

    //축하글 가져오기
    public LetterListResponseDTO getLetters(String limit, LocalDateTime lastUpdatedAt, Long lastLetterId) {
        int fetchCount = "all".equalsIgnoreCase(limit) ? Integer.MAX_VALUE : Integer.parseInt(limit);
        List<Letter> letters;
        int queryCount = fetchCount + 1;

        if (lastUpdatedAt == null || lastLetterId == null) {
            letters = letterRepository.findTopByOrderByUpdatedAtDesc(queryCount);
        } else {
            letters = letterRepository.findByUpdatedAtAndIdBeforeOrderByUpdatedAtDesc(lastUpdatedAt, lastLetterId, queryCount);
        }

        boolean isLast = letters.size() <= fetchCount;
        if (!isLast) {
            letters = letters.subList(0, fetchCount);
        }

        List<LetterResponseDTO> content = letters.stream()
                .map(LetterResponseDTO::from)
                .collect(Collectors.toList());

        Long nextLastLetterId = content.isEmpty() ? null : content.get(content.size() - 1).getId();
        LocalDateTime nextLastUpdatedAt = content.isEmpty() ? null : content.get(content.size() - 1).getCreatedAt();

        return new LetterListResponseDTO(content, isLast, nextLastLetterId, nextLastUpdatedAt);
    }

    //특정 앨범에 대한 축하글 조회
    public LetterListResponseDTO getLettersByAlbum(Long albumId, String limit) {
        int fetchCount = "all".equalsIgnoreCase(limit) ? Integer.MAX_VALUE : Integer.parseInt(limit);

        List<Letter> letters = letterRepository.findByAlbumIdOrderByUpdatedAtDesc(albumId, fetchCount);

        List<LetterResponseDTO> content = letters.stream()
                .map(LetterResponseDTO::from)
                .collect(Collectors.toList());

        return new LetterListResponseDTO(content, true, null, null); // isLast = true로 고정
    }






}
