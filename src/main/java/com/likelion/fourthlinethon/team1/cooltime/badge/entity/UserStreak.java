package com.likelion.fourthlinethon.team1.cooltime.badge.entity;

import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_streak")
public class UserStreak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 유저 */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /** 현재 연속 일수 */
    @Column(name = "current_streak", nullable = false)
    private int currentStreak;

    /** 최대(역대) 연속 일수 */
    @Column(name = "longest_streak", nullable = false)
    private int longestStreak;

    /** 마지막 기록일 */
    @Column(name = "last_record_date")
    private LocalDate lastRecordDate;

    /** 유저에 대한 신규 streak 생성 팩토리 */
    public static UserStreak newOf(User user) {
        return UserStreak.builder()
                .user(user)
                .currentStreak(0)
                .longestStreak(0)
                .lastRecordDate(null)
                .build();
    }

    /** streak 증가 */
    public void increaseStreak() {
        this.currentStreak++;
        if (this.currentStreak > this.longestStreak) {
            this.longestStreak = this.currentStreak;
        }
    }

    /** streak 초기화 */
    public void resetStreak() {
        this.currentStreak = 1;
    }

    /** 연속 끊김 처리 */
    public void clearStreak() {
        this.currentStreak = 0;
    }

    /** 마지막 기록일 갱신 */
    public void updateLastRecordDate(LocalDate date) {
        this.lastRecordDate = date;
    }
}
