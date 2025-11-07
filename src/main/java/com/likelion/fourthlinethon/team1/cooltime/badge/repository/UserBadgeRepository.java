package com.likelion.fourthlinethon.team1.cooltime.badge.repository;

import com.likelion.fourthlinethon.team1.cooltime.badge.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    /** 사용자별 최고 배지 조회 */
    Optional<UserBadge> findTopByUserIdOrderByBadgeDesc(Long userId);

    boolean existsByUserIdAndBadgeCode(Long userId, String badgeCode);
}
