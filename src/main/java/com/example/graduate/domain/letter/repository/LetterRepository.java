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
    @Query("SELECT l FROM Letter l ORDER BY COALESCE(l.updatedAt, l.createdAt) DESC, l.id DESC")
    List<Letter> findTopByOrderByUpdatedAtDesc(Pageable pageable);

    @Query("""
    SELECT l FROM Letter l 
    WHERE (COALESCE(l.updatedAt, l.createdAt) < :lastUpdatedAt)
       OR (COALESCE(l.updatedAt, l.createdAt) = :lastUpdatedAt AND l.id < :lastLetterId)
    ORDER BY COALESCE(l.updatedAt, l.createdAt) DESC, l.id DESC
""")
    List<Letter> findByUpdatedAtAndIdBeforeOrderByUpdatedAtDesc(
            @Param("lastUpdatedAt") LocalDateTime lastUpdatedAt,
            @Param("lastLetterId") Long lastLetterId,
            Pageable pageable
    );

    default List<Letter> findTopByOrderByUpdatedAtDesc(int limit) {
        return findTopByOrderByUpdatedAtDesc(PageRequest.of(0, limit));
    }

    default List<Letter> findByUpdatedAtAndIdBeforeOrderByUpdatedAtDesc(LocalDateTime lastUpdatedAt, Long lastLetterId, int limit) {
        return findByUpdatedAtAndIdBeforeOrderByUpdatedAtDesc(lastUpdatedAt, lastLetterId, PageRequest.of(0, limit));
    }
}
