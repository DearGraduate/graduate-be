package com.example.graduate.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "졸축위 API 명세서",
                description = "졸업축하위원회 API 문서입니다.",
                version = "v1",
                contact = @Contact(
                        name = "졸업축하위원회",
                        url = "http://localhost:3000"
                )
        ),
        security = @SecurityRequirement(name = "bearerAuth"),
        servers = {
                @Server(url = "http://localhost:8080", description = "로컬 서버"),
                @Server(url = "https://api.도메인주소.com", description = "운영 서버")
        }
)
@SecurityScheme(
        name = "bearerAuth", // 보안 스키마 이름 설정
        type = SecuritySchemeType.HTTP, // HTTP 스키마 유형 설정
        scheme = "bearer", // 인증 방식 설정
        bearerFormat = "JWT" // 베어러 형식 설정
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("Swagger API") // API 그룹명
                .pathsToMatch("/api/**", "/swagger-ui/**", "/v3/api-docs/**") // 해당 경로만 문서화
                .build();
    }
}
