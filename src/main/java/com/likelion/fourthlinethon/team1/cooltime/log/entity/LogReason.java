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
    name = "logs_reasons",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_log_reason", columnNames = {"log_id", "reason_id"})
    }
)
public class LogReason extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id", nullable = false)
    private DailyLog log;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reason_id", nullable = false)
    private ReasonTag reason;
}
