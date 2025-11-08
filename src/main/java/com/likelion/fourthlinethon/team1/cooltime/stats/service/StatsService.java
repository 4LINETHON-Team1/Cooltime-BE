package com.likelion.fourthlinethon.team1.cooltime.stats.service;

import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.*;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.DailyLogRepository;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.LogActivityRepository;
import com.likelion.fourthlinethon.team1.cooltime.stats.dto.response.*;
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

    public DashboardOverviewResponse getDashboardOverview(User user) {
        log.info("[서비스] 대시보드 개요 조회 시도 - userId: {}", user.getId());

        // 1) 총 미룸 기록 일수 요약 조회
        TotalRecordSummaryResponse totalRecordSummary = getTotalRecordSummary(user);

        // 2) 최고 카테고리 조회
        TopCategoryResponse topCategory = getTopCategory(user);

        // 3) 전체 미룸 비율 집계 조회
        PostponeRatioTotalResponse postponeRatioTotal = getPostponeRatioTotal(user);
        int postponedPercent = postponeRatioTotal.getTotal().getPostponedPercent();

        // 4) AI 리포트 사용 가능 여부 조회 (임시)
        boolean aiReportAvailable = isAiReportAvailable(user);

        // 5) 응답 생성 및 반환
        return DashboardOverviewResponse.from(
                totalRecordSummary,
                topCategory,
                postponedPercent,
                aiReportAvailable
        );
    }

    private boolean isAiReportAvailable(User user) {
        // AI 리포트 사용 가능 여부 로직 구현 (예: 프리미엄 사용자 여부 등)
        return false; // 기본값으로 false 반환
    }

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

    public PostponeRatioTotalResponse getPostponeRatioTotal(User user) {
        // 1) 기간 설정
        LocalDate startDate = user.getCreatedAt().toLocalDate();
        LocalDate endDate = LocalDate.now();

        // 2) 미룸 비율 집계 조회
        var counts = dailyLogRepository.getPostponeRatioCounts(
                user.getId(),
                startDate,
                endDate
        );
        log.info(("[서비스] 전체 미룸 비율 집계 조회 완료 - userId: {}, counts: {}"), user.getId(), counts);
        if (counts.getTotal()==0L) return PostponeRatioTotalResponse.from(PostponedRatioSummary.empty());
        PostponedRatioSummary summary = PostponedRatioSummary.from(counts);

        return PostponeRatioTotalResponse.from(summary);
    }

    public PostponeRatioWeekResponse getPostponeRatioWeek(User user, WeekPeriod period) {
        // 1) 오늘/가입일 및 기간 유효성 판정 (가입 이전/미래 차단, 가입 주/현재 주 보정)
        LocalDate signup = user.getCreatedAt().toLocalDate();
        LocalDate today = LocalDate.now();

        ClampedPeriod clamped = PeriodGuard.clamp(signup, period, today);
        if (clamped.invalid()) {
            throw new IllegalArgumentException("요청한 기간은 조회할 수 없습니다.");
        }

        // 2) 기준 주 데이터 조회 (기록이 없더라도 요약은 0%로 생성)
        var currentCounts = dailyLogRepository.getPostponeRatioCounts(
                user.getId(), clamped.getStart(), clamped.getEnd()
        );
        PostponedRatioSummary currentSummary = (currentCounts.getTotal() == 0)
                ? PostponedRatioSummary.of(0, 0, 0)
                : PostponedRatioSummary.from(currentCounts);

        // 3) 이전 주 기간 계산 및 조회 (가입 이전/미래인 경우 제외)
        WeekPeriod prevPeriod = (WeekPeriod) period.prev();
        ClampedPeriod prevClamped = PeriodGuard.clamp(signup, prevPeriod, today);

        PostponedRatioSummary previousSummary = null;
        if (!prevClamped.invalid()) {
            var prevCounts = dailyLogRepository.getPostponeRatioCounts(
                    user.getId(), prevClamped.getStart(), prevClamped.getEnd()
            );
            if (prevCounts != null) {
                long total = prevCounts.getTotal();
                if (total > 0) {
                    previousSummary = PostponedRatioSummary.from(prevCounts);
                }
            }
        }

        // 4) 응답 생성 (기간은 보정된 start/end 사용)
        return PostponeRatioWeekResponse.of(
            clamped.getStart(),
            clamped.getEnd(),
            currentSummary,
            previousSummary
        );
    }

    public PostponeRatioMonthResponse getPostponeRatioMonth(User user, MonthPeriod period) {
        LocalDate signup = user.getCreatedAt().toLocalDate();
        LocalDate today = LocalDate.now();

        ClampedPeriod clamped = PeriodGuard.clamp(signup, period, today);
        if (clamped.invalid()) {
            throw new IllegalArgumentException("요청한 기간은 조회할 수 없습니다.");
        }

        var currentCounts = dailyLogRepository.getPostponeRatioCounts(
                user.getId(), clamped.getStart(), clamped.getEnd()
        );
        PostponedRatioSummary currentSummary = (currentCounts.getTotal() == 0)
                ? PostponedRatioSummary.of(0, 0, 0)
                : PostponedRatioSummary.from(currentCounts);

        MonthPeriod prevPeriod = (MonthPeriod) period.prev();
        ClampedPeriod prevClamped = PeriodGuard.clamp(signup, prevPeriod, today);

        PostponedRatioSummary previousSummary = null;
        if (!prevClamped.invalid()) {
            var prevCounts = dailyLogRepository.getPostponeRatioCounts(
                    user.getId(), prevClamped.getStart(), prevClamped.getEnd()
            );
            if (prevCounts != null && prevCounts.getTotal() > 0) {
                previousSummary = PostponedRatioSummary.from(prevCounts);
            }
        }

        return PostponeRatioMonthResponse.of(
                clamped.getStart(),
                clamped.getEnd(),
                currentSummary,
                previousSummary
        );
    }

    public PostponeRatioYearResponse getPostponeRatioYear(User user, YearPeriod period) {
        LocalDate signup = user.getCreatedAt().toLocalDate();
        LocalDate today = LocalDate.now();

        ClampedPeriod clamped = PeriodGuard.clamp(signup, period, today);
        if (clamped.invalid()) {
            throw new IllegalArgumentException("요청한 기간은 조회할 수 없습니다.");
        }

        int year = period.getStart().getYear();
        List<PostponedRatioSummary> monthlySummaries = new ArrayList<>(12);
        for (int m = 1; m <= 12; m++) {
            MonthPeriod mp = MonthPeriod.of(year, m);
            // 월 기간이 클램프 범위와 겹치지 않으면 0 요약
            if (mp.getEnd().isBefore(clamped.getStart()) || mp.getStart().isAfter(clamped.getEnd())) {
                monthlySummaries.add(PostponedRatioSummary.of(0, 0, 0));
                continue;
            }
            LocalDate qs = mp.getStart().isBefore(clamped.getStart()) ? clamped.getStart() : mp.getStart();
            LocalDate qe = mp.getEnd().isAfter(clamped.getEnd()) ? clamped.getEnd() : mp.getEnd();

            var counts = dailyLogRepository.getPostponeRatioCounts(user.getId(), qs, qe);
            if (counts.getTotal() == 0) {
                monthlySummaries.add(PostponedRatioSummary.of(0, 0, 0));
            } else {
                monthlySummaries.add(PostponedRatioSummary.from(counts));
            }
        }

        // 전년도 요약 (무기록이면 null)
        YearPeriod prevYear = (YearPeriod) period.prev();
        ClampedPeriod prevClamped = PeriodGuard.clamp(signup, prevYear, today);
        PostponedRatioSummary previousYearSummary = null;
        if (!prevClamped.invalid()) {
            var prevCounts = dailyLogRepository.getPostponeRatioCounts(user.getId(), prevClamped.getStart(), prevClamped.getEnd());
            if (prevCounts != null && prevCounts.getTotal() > 0) {
                previousYearSummary = PostponedRatioSummary.from(prevCounts);
            }
        }

        return PostponeRatioYearResponse.of(
                clamped.getStart(),
                clamped.getEnd(),
                monthlySummaries,
                previousYearSummary
        );
    }
}
