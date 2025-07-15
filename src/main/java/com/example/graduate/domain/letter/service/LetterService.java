package com.example.graduate.domain.letter.service;

import com.example.graduate.domain.album.domain.Album;
import com.example.graduate.domain.album.repository.AlbumRepository;
import com.example.graduate.domain.letter.domain.Letter;
import com.example.graduate.domain.letter.dto.LetterCreateRequestDTO;
import com.example.graduate.domain.letter.dto.LetterUpdateRequestDTO;
import com.example.graduate.domain.letter.repository.LetterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;
    private final AlbumRepository albumRepository;

    public void createLetter(Long albumId, LetterCreateRequestDTO requestDTO){
        Album album = albumRepository.findById(albumId)
                .orElseThrow(()-> new EntityNotFoundException("해당 앨범이 존재하지 않습니다."));

        Letter letter = Letter.builder()
                .writerName(requestDTO.getWriter_name())
                .picUrl(requestDTO.getPic_url())
                .message(requestDTO.getMessage())
                .isPublic(requestDTO.isPublic())
                .albumId(albumId)
                .build();
        letterRepository.save(letter);
    }

    //축하글 수정
    public void updateLetter(Long letterId, LetterUpdateRequestDTO requestDTO){
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new EntityNotFoundException("수정할 축하글을 찾을 수 없습니다."));

        if (requestDTO.getWriter_name() != null) {
            letter.setWriterName(requestDTO.getWriter_name());
        }
        if (requestDTO.getPic_url() != null) {
            letter.setPicUrl(requestDTO.getPic_url());
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
                .orElseThrow(() -> new EntityNotFoundException("삭제할 축하글을 찾을 수 없습니다."));

        letterRepository.delete(letter);
    }

}
