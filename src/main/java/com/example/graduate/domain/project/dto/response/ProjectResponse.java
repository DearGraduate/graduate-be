package com.example.graduate.domain.project.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectResponse {
  private final long totalAlbumCount;
  private final long totalLetterCount;
}
