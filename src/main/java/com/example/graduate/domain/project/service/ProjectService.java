package com.example.graduate.domain.project.service;


import com.example.graduate.domain.project.dto.response.ProjectResponse;
import com.example.graduate.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

  private final RedisUtil redisUtil;

  private static final String ALBUM_COUNT_KEY = "project:album:count";
  private static final String LETTER_COUNT_KEY = "project:letter:count";

  public ProjectResponse getSummary() {

    long albumCount = redisUtil.getLongValue(ALBUM_COUNT_KEY);
    long letterCount = redisUtil.getLongValue(LETTER_COUNT_KEY);

    return new ProjectResponse(albumCount, letterCount);
  }
}