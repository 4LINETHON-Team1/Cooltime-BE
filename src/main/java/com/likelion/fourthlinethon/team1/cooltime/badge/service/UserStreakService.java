package com.likelion.fourthlinethon.team1.cooltime.badge.service;

import com.likelion.fourthlinethon.team1.cooltime.badge.entity.UserStreak;
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

@Service
@AllArgsConstructor
@Slf4j
public class UserStreakService {
    private final UserRepository userRepository;
    private final UserStreakRepository userStreakRepository;
    private final UserBadgeService userBadgeService;

    @Transactional
    public void updateStreakOnRecord(Long userId) {
        log.info("[서비스] 연속 기록 갱신 시도 - userId: {}", userId);

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        // 유저 streak 조회 또는 신규 생성
        UserStreak streak  = userStreakRepository.findByUserId(userId)
                .orElseGet(() -> userStreakRepository.save(UserStreak.newOf(user)));

        LocalDate today = LocalDate.now();
        LocalDate last = streak.getLastRecordDate();

        // 연속 기록 갱신 로직
        if (last == null) {
            // 첫 기록
            streak.increaseStreak();
        } else if (last.isEqual(today)) {
            // 중복 기록
            return;
        } else if (last.plusDays(1).isEqual(today)) {
            // 어제에 이어 연속
            streak.increaseStreak();
        } else if (last.isBefore(today.minusDays(1))) {
            // 끊김 후 새로 시작 (최소 2일 이상 공백 존재)
            streak.resetStreak();
        } else if (last.isAfter(today)) {
            // 미래 기록 방어
            streak.resetStreak();
        }

        // 마지막 기록일 갱신 및 저장
        streak.updateLastRecordDate(today);
        userStreakRepository.save(streak);

        log.info("[서비스] 연속 기록 갱신 완료 - userId: {}, currentStreak: {}", userId, streak.getCurrentStreak());

        // 배지 판정
        userBadgeService.checkAndAwardOnStreak(user, streak.getCurrentStreak());

    }
}
