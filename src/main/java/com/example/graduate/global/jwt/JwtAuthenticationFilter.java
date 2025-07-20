package com.example.graduate.global.jwt;


import com.example.graduate.domain.user.exception.UserErrorStatus;
import com.example.graduate.global.apiPayload.exception.GeneralException;
import com.example.graduate.global.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
      throws ServletException, IOException {

    // 1. Authorization 헤더에서 AccessToken 추출
    String bearer = request.getHeader("Authorization");
    if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
      String token = bearer.substring(7);

      try {
        // 2. 토큰 유효성 검사
        if (!jwtUtil.isExpired(token)) {
          // 3. 사용자 정보 추출
          Long socialId = jwtUtil.getSocialId(token);
          String name = jwtUtil.getName(token);
          String role = jwtUtil.getRole(token);


          // 4. 인증 객체 생성
          CustomUserDetails userDetails = new CustomUserDetails(socialId, name, role);
          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);

          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          // 5. SecurityContext에 저장
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }

      } catch (io.jsonwebtoken.ExpiredJwtException e) {
        throw new GeneralException(UserErrorStatus.TOKEN_EXPIRED);

      } catch (Exception e) {
        throw new GeneralException(UserErrorStatus.TOKEN_FAIL);
      }
    }

    // 6. 다음 필터로 진행
    filterChain.doFilter(request, response);
  }
}
