package com.likelion.fourthlinethon.team1.cooltime.badge.entity;

import com.likelion.fourthlinethon.team1.cooltime.global.common.BaseTimeEntity;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
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
@Table(
        name = "user_badges",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "ux_user_badges_user_badge",
                        columnNames = {"user_id", "badge_id"}
                )
        },
        indexes = {
                @Index(name = "idx_user_badges_user_id", columnList = "user_id"),
                @Index(name = "idx_user_badges_badge_id", columnList = "badge_id")
        }
)
public class UserBadge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 유저 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** 획득한 배지 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;

}