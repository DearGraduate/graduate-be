package com.example.graduate.domain.album.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AlbumRequestDTO {
    private LocalDate graduationDate;
    private String albumName;
    private String description;
}
