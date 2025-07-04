package com.example.graduate.global.config;

import com.example.graduate.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                //csrf disable (jwt방식은 세션을 stateless상태로 관리하기 때문에 csrf에 대한 공격을 방어하지 않아도 된다.)
                .csrf((auth) -> auth.disable())
                .formLogin((auth) -> auth.disable())//From 로그인 방식 disable
                .httpBasic((auth) -> auth.disable())//http basic 인증 방식
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));//세션 설정
        //인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui/index.html").permitAll() // Swagger 관련 URL 허용
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/api/test/permit-all").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

}
