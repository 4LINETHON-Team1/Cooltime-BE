package com.likelion.fourthlinethon.team1.cooltime.report.entity;

import com.likelion.fourthlinethon.team1.cooltime.global.common.BaseTimeEntity;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.WeekPeriod;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "ai_weekly_reports",
        uniqueConstraints = {
                @UniqueConstraint(name = "ux_user_week", columnNames = {"user_id", "week_start"})
        },
        indexes = {
                @Index(name = "idx_user_recent", columnList = "user_id, week_start DESC")
        }
)
public class AiWeeklyReport extends BaseTimeEntity {

    /** 레포트 ID (PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 유저 ID (FK) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** 주 시작일 (월요일 기준) */
    @Column(name = "week_start", nullable = false)
    private LocalDate weekStart;

    /** 주 종료일 (week_start + 6일) */
    @Column(name = "week_end", nullable = false)
    private LocalDate weekEnd;

    /** UI 라벨 정보 */
    @Column(name = "label_year")
    private Integer labelYear;

    @Column(name = "label_month")
    private Integer labelMonth;

    @Column(name = "week_of_month")
    private Integer weekOfMonth;

    /** 레포트 본문 */
    @Column(name = "pattern_analysis", columnDefinition = "TEXT")
    private String patternAnalysis;

    @Column(name = "solution", columnDefinition = "TEXT")
    private String solution;

    @Column(name = "weekly_comparison", columnDefinition = "TEXT")
    private String weeklyComparison;


    public static AiWeeklyReport createReport(
            User user,
            LocalDate weekStart,
            String patternAnalysis,
            String solution,
            String weeklyComparison
    ) {
        LocalDate weekEnd = weekStart.plusDays(6);
        WeekPeriod weekPeriod = WeekPeriod.of(weekStart);
        LocalDate labelAnchor = weekPeriod.getStart().plusDays(3);

        return AiWeeklyReport.builder()
                .user(user)
                .weekStart(weekStart)
                .weekEnd(weekEnd)
                .labelYear(labelAnchor.getYear())
                .labelMonth(labelAnchor.getMonthValue())
                .weekOfMonth(weekPeriod.weekOfMonthAuto())
                .patternAnalysis(patternAnalysis)
                .solution(solution)
                .weeklyComparison(weeklyComparison)
                .build();
    }



}