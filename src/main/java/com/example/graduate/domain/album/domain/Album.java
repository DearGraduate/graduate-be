package com.example.graduate.domain.album.domain;

import ch.qos.logback.core.Layout;
import com.example.graduate.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "album")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Album extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Setter
    @Column(nullable = false)
    private LocalDate graduationDate;

    @Setter
    @Column(length = 5)
    private String albumName;

    @Setter
    @Column(length = 20)
    private String description;
}

