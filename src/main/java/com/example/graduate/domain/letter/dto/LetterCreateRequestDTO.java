package com.example.graduate.domain.letter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LetterCreateRequestDTO {
    private String writer_name;
    private String pic_url;
    private String message;
    private boolean isPublic;
}
