package com.example.graduate.domain.test.controller;

import com.example.graduate.global.apiPayload.ApiResponse;
import com.example.graduate.global.apiPayload.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Tag(name = "테스트 API입니다", description = "테스트 API입니다")
public class TestController {

    @GetMapping
    @Operation(summary = "api 테스트", description = "api 테스트!!!")
    public ApiResponse<Null> test() {
        return ApiResponse.of(SuccessStatus._OK);
    }
}
