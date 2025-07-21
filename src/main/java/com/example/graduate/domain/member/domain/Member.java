package com.example.graduate.domain.member.domain;


import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String socialId;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 200, nullable = false)
    private String email;

    @Column(nullable = false)
    private Timestamp createAt;

    @Column(nullable = true)
    private Timestamp updateAt;
}
