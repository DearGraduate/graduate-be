package com.example.graduate.domain.letter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LetterUpdateRequestDTO {
    @NotBlank(message = "작성자 이름은 필수입니다.")
    @Size(max = 10, message = "작성자 이름은 최대 10자까지 입력할 수 있습니다.")
    private String writerName;

    @NotBlank(message = "사진 URL은 필수입니다.")
    @Size(max = 1000, message = "사진 URL은 최대 1000자까지 입력할 수 있습니다.")
    private String picUrl;

    @NotBlank(message = "내용은 필수입니다.")
    @Size(max = 300, message = "내용은 최대 300자까지 입력할 수 있습니다.")
    private String message;

    @NotNull(message = "공개 여부는 필수입니다.")
    private Boolean isPublic;
}
