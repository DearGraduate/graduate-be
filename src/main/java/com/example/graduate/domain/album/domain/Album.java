package com.example.graduate.domain.album.domain;

import com.example.graduate.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Entity
@Table(name = "album")
public class Album extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date graduationDate;

    @Column(length = 5, nullable = false)
    private String albumName;

    @Column(length = 20, nullable = false)
    private String description;

    @Column(nullable = false)
    private Long memberId;
}

