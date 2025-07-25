package com.example.graduate.domain.letter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class LetterListResponseDTO {
    private List<LetterResponseDTO> letters;
    private boolean isLast;
    private Long lastLetterId;
    private LocalDateTime lastUpdatedAt;
}

