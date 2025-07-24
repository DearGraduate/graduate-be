package com.example.graduate.domain.album.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AlbumResponseDTO {
    private Long id;
    private String albumName;
    private String description;
    private LocalDate graduationDate;
    private LocalDateTime createdAt;
}
