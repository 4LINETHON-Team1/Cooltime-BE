package com.likelion.fourthlinethon.team1.cooltime.stats.service;

import com.likelion.fourthlinethon.team1.cooltime.log.repository.DailyLogRepository;
import com.likelion.fourthlinethon.team1.cooltime.stats.dto.response.TotalRecordSummaryResponse;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsService {
    private final DailyLogRepository dailyLogRepository;

    public TotalRecordSummaryResponse getTotalRecordSummary(User user) {
        log.info("[서비스] 총 미룸 기록 일수 조회 시도 - userId: {}", user.getId());
        // 1) 사용자 가입일 조회
        LocalDate signupDate = user.getCreatedAt().toLocalDate();

        // 2) 기록한 총 일수 조회
        int recordedDays = (int)dailyLogRepository.countByUser(user);

        // 3) 응답 생성 (가입일 포함해 총일수 계산)
        return TotalRecordSummaryResponse.of(
                signupDate,
                LocalDate.now(),
                recordedDays
        );
    }
}
