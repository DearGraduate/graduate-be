package com.example.graduate.domain.letter.service;

import com.example.graduate.domain.album.domain.Album;
import com.example.graduate.domain.album.repository.AlbumRepository;
import com.example.graduate.domain.letter.domain.Letter;
import com.example.graduate.domain.letter.dto.LetterCreateRequestDTO;
import com.example.graduate.domain.letter.dto.LetterResponseDTO;
import com.example.graduate.domain.letter.dto.LetterUpdateRequestDTO;
import com.example.graduate.domain.letter.repository.LetterRepository;
import com.example.graduate.global.apiPayload.exception.GeneralException;
import com.example.graduate.global.apiPayload.status.letter.LetterErrorStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;
    private final AlbumRepository albumRepository;

    public void createLetter(Long albumId, LetterCreateRequestDTO requestDTO){
        albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(LetterErrorStatus.ALBUM_NOT_FOUND));

        Letter letter = Letter.builder()
                .writerName(requestDTO.getWriterName())
                .picUrl(requestDTO.getPicUrl())
                .message(requestDTO.getMessage())
                .isPublic(requestDTO.getIsPublic())
                .albumId(albumId)
                .build();
        letterRepository.save(letter);
    }

    //축하글 수정
    public void updateLetter(Long letterId, LetterUpdateRequestDTO requestDTO){
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new GeneralException(LetterErrorStatus.LETTER_NOT_FOUND));

        if (requestDTO.getWriterName() != null) {
            letter.setWriterName(requestDTO.getWriterName());
        }
        if (requestDTO.getPicUrl() != null) {
            letter.setPicUrl(requestDTO.getPicUrl());
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
    public void deleteLetter(Long letterId) {
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new GeneralException(LetterErrorStatus.LETTER_NOT_FOUND));

        letterRepository.delete(letter);
    }

    //축하글 가져오기
    public List<LetterResponseDTO> getLetters(String limit, LocalDateTime lastCreatedAt) {
        int fetchCount = "all".equalsIgnoreCase(limit) ? Integer.MAX_VALUE : Integer.parseInt(limit);
        List<Letter> letters;

        if (lastCreatedAt == null) {
            letters = letterRepository.findTopByOrderByCreatedAtDesc(fetchCount);
        } else {
            letters = letterRepository.findByCreatedAtBeforeOrderByCreatedAtDesc(lastCreatedAt, fetchCount);
        }

        return letters.stream()
                .map(LetterResponseDTO::from)
                .collect(Collectors.toList());
    }

}
