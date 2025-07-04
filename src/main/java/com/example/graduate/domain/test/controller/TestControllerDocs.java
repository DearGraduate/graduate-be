package com.example.graduate.domain.test.controller;

import com.example.graduate.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Null;

@Tag(name = "테스트 API입니다", description = "테스트 API입니다")
public interface TestControllerDocs {

    @Operation(summary = "api 테스트", description = "api 시큐리티(permit) 테스트!!!")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "응답 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "isSuccess": true,
                                  "code": "COMMON200",
                                  "message": "성공입니다."
                                }
                            """)
                    )
            )
    })
    public ApiResponse<Null> permitTest();

    @Operation(summary = "api 테스트", description = "api 시큐리티(authenticate) 테스트!!!")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "응답 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "isSuccess": true,
                                  "code": "COMMON200",
                                  "message": "성공입니다."
                                }
                            """)
                    )
            )
    })
    public ApiResponse<Null> authenticateTest();
}
