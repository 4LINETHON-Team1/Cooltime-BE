package com.likelion.fourthlinethon.team1.cooltime.log.entity;

import com.likelion.fourthlinethon.team1.cooltime.global.common.BaseTimeEntity;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.MyType;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
    name = "daily_logs",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_date", columnNames = {"user_id", "date"})
    },
    indexes = {
        @Index(name = "idx_user_postponed", columnList = "user_id, isPostponed")
    }
)
public class DailyLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private boolean isPostponed;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MyType type;

    public void update(Boolean isPostponed, MyType type) {
        this.isPostponed = isPostponed;
        this.type = type;
    }
}
