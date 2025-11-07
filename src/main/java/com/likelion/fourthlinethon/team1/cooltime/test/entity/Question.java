package com.likelion.fourthlinethon.team1.cooltime.test.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;  // 질문 내용

    @Column(nullable = false)
    private int orderNum;    // 질문 순서 (1~7)

    @Column(nullable = false)
    private int weight;      // 가중치 (1 또는 2)
}
