package com.likelion.fourthlinethon.team1.cooltime.log.entity;

import com.likelion.fourthlinethon.team1.cooltime.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
    name = "logs_activities",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_log_activity", columnNames = {"log_id", "activity_id"})
    }
)
public class LogActivity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id", nullable = false)
    private DailyLog log;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private ActivityTag activity;
}
