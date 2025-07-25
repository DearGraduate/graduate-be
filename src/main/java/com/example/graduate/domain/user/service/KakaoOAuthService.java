package com.example.graduate.domain.user.service;

import com.example.graduate.domain.user.dto.request.KakaoUserInfo;
import com.example.graduate.global.config.KakaoOAuthProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {
  private final KakaoOAuthProperties props;
  private final RestTemplate restTemplate;

  @Value("${kakao.admin-key}")
  private String kakaoAdminKey;

  /**
   * 카카오 인가 URI 생성
   */
  public String getAuthorizationUrl() {
    return "https://kauth.kakao.com/oauth/authorize" +
        "?response_type=code" +
        "&client_id=" + props.getClientId() +
        "&redirect_uri=" + props.getRedirectUri();
  }

  /**
   * 인가 코드로 Access Token 발급
   */
  public String getAccessToken(String code) throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
    form.add("grant_type", "authorization_code");
    form.add("client_id", props.getClientId());
    form.add("redirect_uri", props.getRedirectUri());
    form.add("code", code);
    if (props.getClientSecret() != null) {
      form.add("client_secret", props.getClientSecret());
    }

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);
    ResponseEntity<String> response = restTemplate.postForEntity(props.getTokenUri(), request, String.class);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode json = mapper.readTree(response.getBody());
    return json.get("access_token").asText();
  }

  /**
   * 발급된 Access Token으로 사용자 정보 조회
   */
  public KakaoUserInfo getUserInfo(String accessToken) throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<?> request = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange(
        props.getUserInfoUri(), HttpMethod.POST, request, String.class
    );
    ObjectMapper mapper = new ObjectMapper();
    JsonNode body = mapper.readTree(response.getBody());

    String id = body.get("id").asText();
    String name = body.get("properties").get("nickname").asText();
    String email = body.get("kakao_account").get("email").asText();

    return new KakaoUserInfo(id, name, email);
  }

  public void unlinkKakao(String socialId) {
    String url = "https://kapi.kakao.com/v1/user/unlink";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.set("Authorization", "KakaoAK " + kakaoAdminKey);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("target_id_type", "user_id");
    body.add("target_id", socialId);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
    restTemplate.postForEntity(url, request, String.class);
  }
}
