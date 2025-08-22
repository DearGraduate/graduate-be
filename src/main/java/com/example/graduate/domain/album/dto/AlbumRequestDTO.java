package com.example.graduate.domain.album.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AlbumRequestDTO {
    private LocalDate graduationDate;
    @NotBlank(message = "앨범 제목은 비어 있을 수 없습니다.")
    @Size(max = 5, message = "앨범 제목은 최대 5글자까지 입력 가능합니다.")
    private String albumName;

    @NotBlank(message = "앨범 종류는 비어 있을 수 없습니다.")
    @Size(max = 15, message = "앨범 종류는 최대 15글자까지 입력 가능합니다.")
    private String albumType;

    @Size(max = 100, message = "앨범 설명은 최대 100글자까지 입력 가능합니다.")
    private String description;
}
