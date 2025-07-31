package com.example.graduate.domain.letter.repository;

import com.example.graduate.domain.letter.domain.Letter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LetterRepository extends JpaRepository<Letter, Long> {

    @Query("SELECT l FROM Letter l ORDER BY COALESCE(l.updatedAt, l.createdAt) DESC, l.letterId DESC")
    List<Letter> findTopByOrderByUpdatedAtDesc(Pageable pageable);

    @Query("""
        SELECT l FROM Letter l 
        WHERE (COALESCE(l.updatedAt, l.createdAt) < :lastUpdatedAt)
           OR (COALESCE(l.updatedAt, l.createdAt) = :lastUpdatedAt AND l.letterId < :lastLetterId)
        ORDER BY COALESCE(l.updatedAt, l.createdAt) DESC, l.letterId DESC
    """)
    List<Letter> findByUpdatedAtAndIdBeforeOrderByUpdatedAtDesc(
            @Param("lastUpdatedAt") LocalDateTime lastUpdatedAt,
            @Param("lastLetterId") Long lastLetterId,
            Pageable pageable
    );

    void deleteAllByAlbumId(Long albumId);

    default List<Letter> findTopByOrderByUpdatedAtDesc(int limit) {
        return findTopByOrderByUpdatedAtDesc(PageRequest.of(0, limit));
    }

    default List<Letter> findByUpdatedAtAndIdBeforeOrderByUpdatedAtDesc(LocalDateTime lastUpdatedAt, Long lastLetterId, int limit) {
        return findByUpdatedAtAndIdBeforeOrderByUpdatedAtDesc(lastUpdatedAt, lastLetterId, PageRequest.of(0, limit));
    }

    // 특정 앨범의 축하글 조회 (limit만 사용)
    @Query("""
        SELECT l FROM Letter l 
        WHERE l.albumId = :albumId 
        ORDER BY COALESCE(l.updatedAt, l.createdAt) DESC, l.letterId DESC
    """)
    List<Letter> findByAlbumIdOrderByUpdatedAtDesc(
            @Param("albumId") Long albumId,
            Pageable pageable
    );

    default List<Letter> findByAlbumIdOrderByUpdatedAtDesc(Long albumId, int limit) {
        return findByAlbumIdOrderByUpdatedAtDesc(albumId, PageRequest.of(0, limit));
    }

    // 특정 앨범에서 무한스크롤용 조회 (updatedAt + letterId 기반)
    @Query("""
        SELECT l FROM Letter l
        WHERE l.albumId = :albumId
          AND (
            COALESCE(l.updatedAt, l.createdAt) < :lastUpdatedAt
            OR (COALESCE(l.updatedAt, l.createdAt) = :lastUpdatedAt AND l.letterId < :lastLetterId)
          )
        ORDER BY COALESCE(l.updatedAt, l.createdAt) DESC, l.letterId DESC
    """)
    List<Letter> findByAlbumIdAndUpdatedAtAndIdBeforeOrderByUpdatedAtDesc(
            @Param("albumId") Long albumId,
            @Param("lastUpdatedAt") LocalDateTime lastUpdatedAt,
            @Param("lastLetterId") Long lastLetterId,
            Pageable pageable
    );

    default List<Letter> findByAlbumIdAndUpdatedAtAndIdBeforeOrderByUpdatedAtDesc(
            Long albumId, LocalDateTime lastUpdatedAt, Long lastLetterId, int limit) {
        return findByAlbumIdAndUpdatedAtAndIdBeforeOrderByUpdatedAtDesc(albumId, lastUpdatedAt, lastLetterId, PageRequest.of(0, limit));
    }

    @Query("""
    SELECT l FROM Letter l
    WHERE l.albumId = :albumId
      AND (
        l.userId = :userId
        OR l.isPublic = true
      )
    ORDER BY COALESCE(l.updatedAt, l.createdAt) DESC, l.letterId DESC
""")
    List<Letter> findVisibleLettersByAlbum(
            @Param("albumId") Long albumId,
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("""
    SELECT l FROM Letter l
    WHERE l.albumId = :albumId
      AND (
        l.userId = :userId
        OR l.isPublic = true
      )
      AND (
        COALESCE(l.updatedAt, l.createdAt) < :lastUpdatedAt
        OR (COALESCE(l.updatedAt, l.createdAt) = :lastUpdatedAt AND l.letterId < :lastLetterId)
      )
    ORDER BY COALESCE(l.updatedAt, l.createdAt) DESC, l.letterId DESC
""")
    List<Letter> findVisibleLettersByAlbumAndCursor(
            @Param("albumId") Long albumId,
            @Param("userId") Long userId,
            @Param("lastUpdatedAt") LocalDateTime lastUpdatedAt,
            @Param("lastLetterId") Long lastLetterId,
            Pageable pageable
    );

}


