package com.example.graduate.domain.album.repository;

import com.example.graduate.domain.album.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    List<Album> findByGraduationDateBefore(LocalDate cutoffDate);

}