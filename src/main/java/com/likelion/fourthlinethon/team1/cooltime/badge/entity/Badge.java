package com.likelion.fourthlinethon.team1.cooltime.badge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "badges")
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 배지 코드 (예: STREAK_10) */
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    /** 달성 조건 일수 (예: 10, 30, 50, ...) */
    @Column(name = "streak_days_required", nullable = false)
    private int streakDaysRequired;

}