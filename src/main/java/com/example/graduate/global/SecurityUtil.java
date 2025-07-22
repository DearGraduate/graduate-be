package com.example.graduate.global;

import com.example.graduate.domain.user.entity.User;
import com.example.graduate.domain.user.exception.UserErrorStatus;
import com.example.graduate.domain.user.repository.UserRepository;
import com.example.graduate.global.apiPayload.exception.GeneralException;
import com.example.graduate.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
  private final UserRepository userRepository;

  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication.getPrincipal() == null) {
      throw new SecurityException("사용자 정보를 불러올 수 없습니다.");
    }

    if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
      String socialId = userDetails.getSocialId();
      return userRepository.findBySocialId(socialId)
          .orElseThrow(() -> new GeneralException(UserErrorStatus.USER_NOT_FOUND));
    }

    throw new GeneralException(UserErrorStatus.INVALID_AUTHENTICATION);
  }
}
