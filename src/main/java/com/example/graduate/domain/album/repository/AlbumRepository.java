package com.example.graduate.domain.album.repository;

import com.example.graduate.domain.album.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByuserId(Long userId);
    boolean existsByuserId(Long userId);
}