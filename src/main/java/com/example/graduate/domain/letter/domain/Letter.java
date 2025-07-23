package com.example.graduate.domain.letter.domain;

import com.example.graduate.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Entity
@Table(name = "letter")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Letter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long letterId;

    @Setter
    @Column(length = 10, nullable = false)
    private String writerName;

    @Setter
    @Column(length = 1000, nullable = true)
    private String picUrl;

    @Setter
    @Column(length = 300, nullable = false)
    private String message;

    @Setter
    @Column
    private Boolean isPublic;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long albumId;
}
