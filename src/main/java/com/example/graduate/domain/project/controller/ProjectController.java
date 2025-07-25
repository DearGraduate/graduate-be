package com.example.graduate.domain.project.controller;

import com.example.graduate.domain.project.dto.response.ProjectResponseDTO;
import com.example.graduate.domain.project.service.ProjectService;
import com.example.graduate.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/project")
@Tag(name = "프로젝트 정보 관련 API", description = "프로젝트의 정보에 관련된 기능을 제공합니다.")
public class ProjectController {

  private final ProjectService projectService;

  @Operation(
      summary = "누적 앨범·축하글 수 조회",
      description = "현재까지 생성된 앨범·축하글 수를 조회합니다.")
  @GetMapping("/info")
  public ResponseEntity<ApiResponse<ProjectResponseDTO>> getProjectSummary() {
    ProjectResponseDTO result = projectService.getSummary();

    return ResponseEntity.ok(ApiResponse.onSuccess(result));
  }
}
