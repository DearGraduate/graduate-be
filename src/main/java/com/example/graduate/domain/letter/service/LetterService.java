package com.example.graduate.domain.letter.service;

import com.example.graduate.domain.album.repository.AlbumRepository;
import com.example.graduate.domain.letter.domain.Letter;
import com.example.graduate.domain.letter.dto.LetterCreateRequestDTO;
import com.example.graduate.domain.letter.dto.LetterListResponseDTO;
import com.example.graduate.domain.letter.dto.LetterResponseDTO;
import com.example.graduate.domain.letter.dto.LetterUpdateRequestDTO;
import com.example.graduate.domain.letter.repository.LetterRepository;
import com.example.graduate.global.SecurityUtil;
import com.example.graduate.global.apiPayload.exception.GeneralException;
import com.example.graduate.domain.letter.domain.letterStatus.LetterErrorStatus;
import com.example.graduate.global.aws.s3.AmazonS3Manager;
import com.example.graduate.global.aws.s3.Uuid;
import com.example.graduate.global.aws.s3.UuidRepository;
import com.example.graduate.global.redis.RedisUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;
    private final AlbumRepository albumRepository;
    private final SecurityUtil securityUtil;
    private final RedisUtil redisUtil;

    //s3 관련 코드 추가
    private final AmazonS3Manager amazonS3Manager;
    private final UuidRepository uuidRepository;

    private Long getCurrentUserId() {
        return securityUtil.getCurrentUser().getId();
    }

    //기본 이미지 여부 확인(생성, 수정)
    private boolean isDefaultImage(String lowerName) {
        return lowerName.equals("defaultimage1")
                || lowerName.equals("defaultimage2")
                || lowerName.equals("defaultimage3");
    }

    //확장자 필터링(생성, 수정)
    private boolean hasAllowedExtension(String lowerName) {
        return lowerName.endsWith(".jpg")
                || lowerName.endsWith(".jpeg")
                || lowerName.endsWith(".png");
    }

    //저장된 picUrl이 기본 이미지 식별자인지 확인(수정)
    private boolean isStoredDefaultImage(String picUrl) {
        if (picUrl == null) return false;
        return isDefaultImage(picUrl.toLowerCase(Locale.ROOT));
    }

    //url 판별용(수정, 삭제)
    private boolean isUrl(String value) {
        if (value == null) return false;
        String v = value.toLowerCase(Locale.ROOT);
        return v.startsWith("http://") || v.startsWith("https://");
    }

    //이미지 처리
    private String handlePicUrl(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) return null;

        String lowerName = originalFilename.toLowerCase(Locale.ROOT);

        // 기본 이미지면 업로드 생략
        if (isDefaultImage(lowerName)) {
            return lowerName; // DB에는 기본 이미지 식별자 저장
        }

        // 확장자 화이트리스트 검사
        if (!hasAllowedExtension(lowerName)) {
            throw new GeneralException(LetterErrorStatus.INVALID_FILE_EXTENSION);
        }

        // S3 업로드
        Uuid savedUuid = uuidRepository.save(
                Uuid.builder().uuid(UUID.randomUUID().toString()).build()
        );
        String keyName = amazonS3Manager.generateLetterKeyName(file, savedUuid);
        return amazonS3Manager.uploadFile(keyName, file);
    }

    @Transactional
    public void createLetter(Long albumId, LetterCreateRequestDTO requestDTO, MultipartFile file) {
        albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(LetterErrorStatus.ALBUM_NOT_FOUND));

        //이미지 처리
        String picUrl = handlePicUrl(file);

        //작성자 가져오기
        Long userId = getCurrentUserId();

        //저장
        Letter letter = Letter.builder()
                .writerName(requestDTO.getWriterName())
                .message(requestDTO.getMessage())
                .isPublic(requestDTO.getIsPublic())
                .albumId(albumId)
                .picUrl(picUrl)
                .userId(userId)
                .build();
        letterRepository.save(letter);

        //개수 카운트
        redisUtil.increment("project:letter:count");
    }



    //축하글 수정
    @Transactional
    public void updateLetter(Long letterId, LetterUpdateRequestDTO requestDTO, MultipartFile file) {
        //letter 존재 여부 확인
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new GeneralException(LetterErrorStatus.LETTER_NOT_FOUND));

        //letter 소유자 확인
        if (!letter.getUserId().equals(getCurrentUserId())) {
            throw new GeneralException(LetterErrorStatus.NOT_OWNER_OF_LETTER);
        }

        //이미지 파일이 새로 들어온 경우 기존 S3 이미지 삭제 후 새로 업로드
        if (file != null && !file.isEmpty()) {

            //기존 이미지가 URL로 저장되어 있다면(=기본 이미지 식별자 아님) S3에서 삭제
            if (letter.getPicUrl() != null && !isStoredDefaultImage(letter.getPicUrl()) && isUrl(letter.getPicUrl())) {
                amazonS3Manager.deleteFileByUrl(letter.getPicUrl());
            }

            //생성 로직과 동일한 규칙으로 이미지 업로드
            String newPicUrl = handlePicUrl(file);
            letter.setPicUrl(newPicUrl);
        }

        if (requestDTO.getWriterName() != null) {
            letter.setWriterName(requestDTO.getWriterName());
        }
        if (requestDTO.getMessage() != null) {
            letter.setMessage(requestDTO.getMessage());
        }
        if (requestDTO.getIsPublic() != null) {
            letter.setIsPublic(requestDTO.getIsPublic());
        }

        letterRepository.save(letter);
    }


    //축하글 삭제
    @Transactional
    public void deleteLetter(Long letterId) {
        //축하글 존재하는지 확인
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new GeneralException(LetterErrorStatus.LETTER_NOT_FOUND));

        Long currentUserId = getCurrentUserId();

        //삭제 권한 있는 사람인지 확인(작성자, 앨범 소유자)
        boolean isWriter = letter.getUserId().equals(currentUserId);
        boolean isAlbumOwner = albumRepository.findById(letter.getAlbumId())
                .map(a -> a.getUserId().equals(currentUserId))
            .orElseThrow(() -> new GeneralException(LetterErrorStatus.ALBUM_NOT_FOUND));


        if (!isWriter && !isAlbumOwner) {
            throw new GeneralException(LetterErrorStatus.NOT_OWNER_OF_LETTER);
        }

        //s3에서 이미지 삭제
        String picUrl = letter.getPicUrl();
        if (isUrl(picUrl)) {
            amazonS3Manager.deleteFileByUrl(picUrl);
        }


        letterRepository.delete(letter);
    }

    //축하글 전체 가져오기(이건 사용하지 않습니다)
    public LetterListResponseDTO getLetters(String limit, LocalDateTime lastUpdatedAt, Long lastLetterId) {
        int fetchCount = "all".equalsIgnoreCase(limit) ? Integer.MAX_VALUE : Integer.parseInt(limit);
        List<Letter> letters;
        int queryCount = fetchCount + 1;

        if (lastUpdatedAt == null || lastLetterId == null) {
            letters = letterRepository.findTopByOrderByUpdatedAtDesc(queryCount);
        } else {
            letters = letterRepository.findByUpdatedAtAndIdBeforeOrderByUpdatedAtDesc(lastUpdatedAt, lastLetterId, queryCount);
        }

        boolean isLast = letters.size() <= fetchCount;
        if (!isLast) {
            letters = letters.subList(0, fetchCount);
        }

        List<LetterResponseDTO> content = letters.stream()
                .map(LetterResponseDTO::from)
                .collect(Collectors.toList());

        Long nextLastLetterId = content.isEmpty() ? null : content.get(content.size() - 1).getLetterId();
        LocalDateTime nextLastUpdatedAt = content.isEmpty() ? null : content.get(content.size() - 1).getCreatedAt();

        return new LetterListResponseDTO(content, isLast, nextLastLetterId, nextLastUpdatedAt);
    }


    //특정 앨범에 대한 축하글 조회
    public LetterListResponseDTO getLettersByAlbum(Long albumId, String limit, LocalDateTime lastUpdatedAt, Long lastLetterId) {
        int fetchCount = "all".equalsIgnoreCase(limit) ? Integer.MAX_VALUE : Integer.parseInt(limit);
        int queryCount = fetchCount + 1;
        Long userId = getCurrentUserId();

        boolean isOwner = albumRepository.findById(albumId)
                .map(album -> album.getUserId().equals(userId))
                .orElseThrow(() -> new GeneralException(LetterErrorStatus.ALBUM_NOT_FOUND));

        List<Letter> letters;

        if (isOwner) {
            // 앨범 주인은 모든 글 조회
            if (lastUpdatedAt == null || lastLetterId == null) {
                letters = letterRepository.findByAlbumIdOrderByUpdatedAtDesc(albumId, queryCount);
            } else {
                letters = letterRepository.findByAlbumIdAndUpdatedAtAndIdBeforeOrderByUpdatedAtDesc(albumId, lastUpdatedAt, lastLetterId, queryCount);
            }
        } else {
            // 앨범 주인이 아닌 경우: 본인 글 + 공개된 글만
            if (lastUpdatedAt == null || lastLetterId == null) {
                letters = letterRepository.findVisibleLettersByAlbum(albumId, userId, PageRequest.of(0, queryCount));
            } else {
                letters = letterRepository.findVisibleLettersByAlbumAndCursor(albumId, userId, lastUpdatedAt, lastLetterId, PageRequest.of(0, queryCount));
            }
        }

        boolean isLast = letters.size() <= fetchCount;
        if (!isLast) {
            letters = letters.subList(0, fetchCount);
        }

        List<LetterResponseDTO> content = letters.stream()
                .map(LetterResponseDTO::from)
                .collect(Collectors.toList());

        Long nextLastLetterId = content.isEmpty() ? null : content.get(content.size() - 1).getLetterId();
        LocalDateTime nextLastUpdatedAt = content.isEmpty()
                ? null
                : content.get(content.size() - 1).getUpdatedAt() != null
                ? content.get(content.size() - 1).getUpdatedAt()
                : content.get(content.size() - 1).getCreatedAt();

        return new LetterListResponseDTO(content, isLast, nextLastLetterId, nextLastUpdatedAt);

    }

    //홈화면 letter 가져오기
    public LetterListResponseDTO getHomeLettersByAlbum(Long albumId, String limit,
                                                       LocalDateTime lastUpdatedAt, Long lastLetterId) {
        int fetchCount = "all".equalsIgnoreCase(limit) ? Integer.MAX_VALUE : Integer.parseInt(limit);
        int queryCount = fetchCount + 1; // 다음 페이지 여부 판별용으로 1개 더 조회

        //앨범 있는지 확인
        albumRepository.findById(albumId)
                .orElseThrow(() -> new GeneralException(LetterErrorStatus.ALBUM_NOT_FOUND));

        List<Letter> letters;

        if (lastUpdatedAt == null || lastLetterId == null) {
            letters = letterRepository.findByAlbumIdAndIsPublicTrueOrderByUpdatedAtDesc(
                    albumId, PageRequest.of(0, queryCount)
            );
        } else {
            letters = letterRepository.findPublicLettersByAlbumAndCursor(
                    albumId, lastUpdatedAt, lastLetterId, PageRequest.of(0, queryCount)
            );
        }

        boolean isLast = letters.size() <= fetchCount;
        if (!isLast) {
            letters = letters.subList(0, fetchCount);
        }

        List<LetterResponseDTO> content = letters.stream()
                .map(LetterResponseDTO::from)
                .collect(Collectors.toList());

        Long nextLastLetterId = content.isEmpty() ? null : content.get(content.size() - 1).getLetterId();
        LocalDateTime nextLastUpdatedAt = content.isEmpty()
                ? null
                : (content.get(content.size() - 1).getUpdatedAt() != null
                ? content.get(content.size() - 1).getUpdatedAt()
                : content.get(content.size() - 1).getCreatedAt());

        return new LetterListResponseDTO(content, isLast, nextLastLetterId, nextLastUpdatedAt);
    }

}