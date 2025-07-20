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
    @Query("SELECT l FROM Letter l ORDER BY l.createdAt DESC")
    List<Letter> findTopByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT l FROM Letter l WHERE l.createdAt < :lastCreatedAt ORDER BY l.createdAt DESC")
    List<Letter> findByCreatedAtBeforeOrderByCreatedAtDesc(
            @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
            Pageable pageable
    );

    // 서비스에서 사용하기 편하게 count만 넘기는 wrapper
    default List<Letter> findTopByOrderByCreatedAtDesc(int limit) {
        return findTopByOrderByCreatedAtDesc(PageRequest.of(0, limit));
    }

    default List<Letter> findByCreatedAtBeforeOrderByCreatedAtDesc(LocalDateTime lastCreatedAt, int limit) {
        return findByCreatedAtBeforeOrderByCreatedAtDesc(lastCreatedAt, PageRequest.of(0, limit));
    }
}
