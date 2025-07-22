package com.example.graduate.domain.album.scheduler;

import com.example.graduate.domain.album.repository.AlbumRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlbumCleanupScheduler {
    private final AlbumRepository albumRepository;

    @Scheduled(cron = "0 0 0 * * *") // 매일 00:00:00에 실행
    @Transactional
    public void deleteExpiredAlbums() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<com.example.graduate.domain.album.domain.Album> expiredAlbums = albumRepository.findByGraduationDateBefore(oneMonthAgo);

        if (!expiredAlbums.isEmpty()) {
            albumRepository.deleteAll(expiredAlbums);
            log.info("만료된 앨범 {}개 삭제됨", expiredAlbums.size());
        } else {
            log.info("삭제할 만료 앨범 없음");
        }
    }
}
