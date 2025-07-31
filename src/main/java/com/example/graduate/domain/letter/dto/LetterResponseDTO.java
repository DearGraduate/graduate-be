package com.example.graduate.domain.letter.dto;

import com.example.graduate.domain.letter.domain.Letter;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LetterResponseDTO {
    private Long letterId;
    private String writerName;
    private String picUrl;
    private String message;
    private Boolean isPublic;
    private LocalDateTime createdAt;

    public static LetterResponseDTO from(Letter letter) {
        return LetterResponseDTO.builder()
                .letterId(letter.getLetterId())
                .writerName(letter.getWriterName())
                .picUrl(letter.getPicUrl())
                .message(letter.getMessage())
                .isPublic(letter.getIsPublic())
                .createdAt(letter.getCreatedAt())
                .build();
    }

}
