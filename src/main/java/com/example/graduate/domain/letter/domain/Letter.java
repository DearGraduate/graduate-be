package com.example.graduate.domain.letter.domain;

import com.example.graduate.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "letter")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Letter extends BaseEntity {

    //Long으로 해야 bigint
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String writerName;

    @Column(length = 1000, nullable = true)
    private String picUrl;

    @Column(length = 300, nullable = false)
    private String message;

    @Column
    private Boolean isPublic;

    @Column
    private Long memberId;

    @Column
    private Long albumId;

}
