package com.likelion.fourthlinethon.team1.cooltime.stats.service;

import com.likelion.fourthlinethon.team1.cooltime.log.repository.DailyLogRepository;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.LogActivityRepository;
import com.likelion.fourthlinethon.team1.cooltime.stats.dto.response.CategoryRankItem;
import com.likelion.fourthlinethon.team1.cooltime.stats.dto.response.CategoryStatsAllResponse;
import com.likelion.fourthlinethon.team1.cooltime.stats.dto.response.TopCategoryResponse;
import com.likelion.fourthlinethon.team1.cooltime.stats.dto.response.TotalRecordSummaryResponse;
import com.likelion.fourthlinethon.team1.cooltime.stats.projection.ActivityStatsProjection;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsService {
    private final DailyLogRepository dailyLogRepository;
    private final LogActivityRepository logActivityRepository;

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

    public TopCategoryResponse getTopCategory(User user) {
        log.info("[서비스] 최고 카테고리 조회 시도 - userId: {}", user.getId());

        return logActivityRepository.findTopPostponedActivityByUserId(user.getId())
                .map(proj -> TopCategoryResponse.of(proj.getActivityName()))
                .orElseGet(TopCategoryResponse::empty);
    }

    public CategoryStatsAllResponse getCategoryStatsAll(User user) {
        log.info("[서비스] 카테고리별 미룸 횟수 통계 조회 시도 - userId: {}", user.getId());

        // 1) 카테고리별 '미룸' 집계
        // (activityId, activityName, cnt) 리스트 반환
        var projections = logActivityRepository.findAllPostponedActivityStatsByUserId(user.getId());

        // 미룸 기록이 없는 경우 빈 응답 반환
        if (projections.isEmpty()) {
            return new CategoryStatsAllResponse(new ArrayList<>());
        }

        // 2) 사용자 전체 기록 수 조회
        int totalCount = (int)dailyLogRepository.countByUser(user);

        // 3) Rank 매기면서 DTO 변환
        List<CategoryRankItem> items = new ArrayList<>(projections.size());
        for (int i = 0; i < projections.size(); i++) {
            ActivityStatsProjection p = projections.get(i);
            items.add(
                    CategoryRankItem.builder()
                            .categoryId(p.getActivityId())
                            .categoryName(p.getActivityName())
                            .rank(i + 1) // 1부터 시작
                            .totalCount(totalCount)
                            .postponedCount(Math.toIntExact(p.getCnt()))
                            .build()
            );
        }

        return new CategoryStatsAllResponse(items);
    }
}
