package com.example.graduate.domain.album.domain;

import ch.qos.logback.core.Layout;
import com.example.graduate.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "album", uniqueConstraints = {
        @UniqueConstraint(columnNames = "member_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Album extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDate graduationDate;

    @Column(length = 5)
    private String albumName;

    @Column(length = 20)
    private String description;
}

