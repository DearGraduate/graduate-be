package com.example.graduate.domain.letter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LetterUpdateRequestDTO {
    private String writer_name;  // null 가능
    private String pic_url;      // null 가능
    private String message;      // null 가능
    private Boolean isPublic;    // null 가능 ← 이게 중요!
}
