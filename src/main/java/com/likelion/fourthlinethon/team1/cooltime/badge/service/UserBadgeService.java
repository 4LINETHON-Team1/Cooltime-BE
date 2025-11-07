package com.likelion.fourthlinethon.team1.cooltime.badge.service;

import com.likelion.fourthlinethon.team1.cooltime.badge.dto.response.BadgeListResponse;
import com.likelion.fourthlinethon.team1.cooltime.badge.dto.response.BadgeProgressResponse;
import com.likelion.fourthlinethon.team1.cooltime.badge.entity.Badge;
import com.likelion.fourthlinethon.team1.cooltime.badge.entity.UserBadge;
import com.likelion.fourthlinethon.team1.cooltime.badge.entity.UserStreak;
import com.likelion.fourthlinethon.team1.cooltime.badge.enums.BadgeCode;
import com.likelion.fourthlinethon.team1.cooltime.badge.exception.BadgeErrorCode;
import com.likelion.fourthlinethon.team1.cooltime.badge.repository.UserBadgeRepository;
import com.likelion.fourthlinethon.team1.cooltime.badge.repository.UserStreakRepository;
import com.likelion.fourthlinethon.team1.cooltime.global.exception.CustomException;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import com.likelion.fourthlinethon.team1.cooltime.user.exception.UserErrorCode;
import com.likelion.fourthlinethon.team1.cooltime.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserBadgeService {

    private final UserStreakRepository userStreakRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final BadgeService badgeService;

    /** streak 기반 배지 지급 */
    @Transactional
    public void checkAndAwardOnStreak(User user, int currentStreak) {
        log.info("[서비스] 배지 지급 확인 시도 - userId: {}, currentStreak: {}", user.getId(), currentStreak);
        // 현재 streak로 달성 가능한 최고 배지 찾기
        Optional<BadgeCode> highest = BadgeCode.bestByStreak(currentStreak);

        // 달성 가능한 배지가 없으면 종료
        if (highest.isEmpty()) return;

        // 이미 보유한 배지면 skip
        boolean alreadyEarned = userBadgeRepository.existsByUserIdAndBadgeCode(user.getId(), highest.get().getCode());
        if (alreadyEarned) return;

        // 배지 지급
        Badge badge = badgeService.findByCode(highest.get().getCode());

        userBadgeRepository.save(UserBadge.builder()
                .user(user)
                .badge(badge)
                .build());
        log.info("[서비스] 배지 지급 완료 - userId: {}, badgeCode: {}", user.getId(), highest.get().getCode());
    }

    /** 배지 진행 상황 조회 */
    public BadgeProgressResponse getBadgeProgress(Long userId) {
        log.info("[서비스] 배지 진행 상황 조회 시도 - userId: {}", userId);
        UserStreak userStreak = userStreakRepository.findByUserId(userId).orElse(null);

        // longestStreak 기준으로 표시
        int displayed = (userStreak != null) ? userStreak.getLongestStreak() : 0;

        // longestStreak을 기준으로 최고 배지 계산
        BadgeCode highest = BadgeCode.bestByStreak(displayed).orElse(null);

        log.info("[서비스] 배지 진행 상황 조회 완료 - userId: {}, longestStreak: {}, highestBadge: {}",
                userId, displayed, (highest != null ? highest.getCode() : "NONE"));
        return BadgeProgressResponse.of(displayed, highest);
    }

    /** 사용자 배지 목록 조회 */
    public BadgeListResponse getBadgeList(Long userId) {
        log.info("[서비스] 사용자 배지 목록 조회 시도 - userId: {}", userId);
        // 사용자별 최고 배지 조회
        UserBadge highestBadge = userBadgeRepository.findTopByUserIdOrderByBadgeDesc(userId)
                .orElse(null);

        // 최고 배지 코드 추출
        BadgeCode highestBadgeCode = (highestBadge != null) ? BadgeCode.valueOf(highestBadge.getBadge().getCode()) : null;

        log.info("[서비스] 사용자 배지 목록 조회 완료 - userId: {}, highestBadgeCode: {}",
                userId, (highestBadgeCode != null ? highestBadgeCode.getCode() : "NONE"));
        // BadgeListResponse 생성 및 반환
        return BadgeListResponse.from(highestBadgeCode);
    }
}
