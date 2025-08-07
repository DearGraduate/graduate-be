package com.example.graduate.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 쿠키, 인증정보 포함 여부
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://graduate-web-coral.vercel.app",
                "http://photory.site",
                "https://photory.site",
                "http://api.photory.site",
                "https://api.photory.site"));
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addAllowedMethod("*"); // GET, POST, PUT, DELETE 등 모든 메서드 허용

        config.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 위 CORS 적용

        return source;
    }
}