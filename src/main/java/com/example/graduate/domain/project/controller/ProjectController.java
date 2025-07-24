package com.example.graduate.domain.project.controller;

import com.example.graduate.domain.project.dto.response.ProjectResponse;
import com.example.graduate.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/project")
public class ProjectController {

  private final ProjectService projectService;

  @GetMapping("/info")
  public ResponseEntity<ProjectResponse> getProjectSummary() {
    return ResponseEntity.ok(projectService.getSummary());
  }
}
