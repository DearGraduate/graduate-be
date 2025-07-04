package com.example.graduate.domain.test.controller;

import com.example.graduate.global.apiPayload.ApiResponse;
import com.example.graduate.global.apiPayload.status.SuccessStatus;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController implements TestControllerDocs{

    //응답이 잘 와야되는게 맞음
    @GetMapping("/permit-all")
    public ApiResponse<Null> permitTest() {
        return ApiResponse.of(SuccessStatus._OK);
    }

    //권한이 없다고 나와야 맞음
    @GetMapping("/authenticate")
    public ApiResponse<Null> authenticateTest() {
        return ApiResponse.of(SuccessStatus._OK);
    }
}
