package com.likelion.fourthlinethon.team1.cooltime.stats.service;

import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.*;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.DailyLogRepository;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.LogActivityRepository;
import com.likelion.fourthlinethon.team1.cooltime.stats.dto.response.*;
import com.likelion.fourthlinethon.team1.cooltime.stats.projection.ActivityStatsProjection;
import com.likelion.fourthlinethon.team1.cooltime.stats.projection.PostponeRatioCounts;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
                // 기록이 없거나, 미룸 기록이 없는 경우 빈 응답 반환
                .orElseGet(TopCategoryResponse::empty);
    }

    public CategoryStatsAllResponse getCategoryStatsAll(User user) {
        log.info("[서비스] 카테고리별 미룸 횟수 통계 조회 시도 - userId: {}", user.getId());

        // 1) 카테고리별 '미룸' 집계
        // (activityId, activityName, cnt) 리스트 반환
        var projections = logActivityRepository.findAllPostponedActivityStatsByUserId(user.getId());

        // 기록이 없거나, 미룸 기록이 없는 경우 빈 응답 반환
        if (projections.isEmpty()) {
            return new CategoryStatsAllResponse(new ArrayList<>());
        }

        // 2) 사용자 전체 기록 수 조회
        int totalCount = (int)dailyLogRepository.countByUser(user);

        // 3) Rank 매기면서 DTO 변환
        List<CategoryRankItem> items = IntStream.range(0, projections.size())
                .mapToObj(i ->  CategoryRankItem.of(projections.get(i), i + 1, totalCount))
                .toList();

        return new CategoryStatsAllResponse(items);
    }

    public PostponeRatioTotalResponse getPostponeRatioTotal(User user) {
        // 1) 기간 설정
        LocalDate startDate = user.getCreatedAt().toLocalDate();
        LocalDate endDate = LocalDate.now();

        // 2) 전체 미룸 비율 요약 조회
        PostponedRatioSummary summary = fetchSummary(user.getId(), startDate, endDate);

        return PostponeRatioTotalResponse.from(summary);
    }

    public PostponeRatioWeekResponse getPostponeRatioWeek(User user, WeekPeriod period) {
        // 1) 회원 가입일 및 오늘 날짜 조회
        LocalDate signup = user.getCreatedAt().toLocalDate();
        LocalDate today = LocalDate.now();

        // 2) 기간 클램프 및 유효성 검사
        ClampedPeriod clamped = PeriodGuard.clamp(signup, period, today);

        // 3) 요청 기간 및 유효 기간 생성
        PeriodResponse requestedPeriod = PeriodResponse.of(period.getStart(), period.getEnd());
        PeriodResponse effectivePeriod = PeriodResponse.of(clamped.getStart(), clamped.getEnd());

        if (clamped.invalid()) {
            log.warn("[서비스] 주간 미룸 비율 조회 - 유효하지 않은 기간 요청 - userId: {}, period: {}", user.getId(), period);
            return PostponeRatioWeekResponse.empty(requestedPeriod, effectivePeriod);
        }

        // 4) 현재 및 이전 기간 요약 조회
        PostponedRatioSummary currentSummary = fetchSummary(user.getId(), clamped.getStart(), clamped.getEnd());
        PostponedRatioSummary previousSummary = fetchPreviousSummary(user.getId(), signup, (WeekPeriod) period.prev(), today);

        return PostponeRatioWeekResponse.of(
            requestedPeriod,
            effectivePeriod,
            currentSummary,
            previousSummary
        );
    }

    public PostponeRatioMonthResponse getPostponeRatioMonth(User user, MonthPeriod period) {
        // 1) 회원 가입일 및 오늘 날짜 조회
        LocalDate signup = user.getCreatedAt().toLocalDate();
        LocalDate today = LocalDate.now();

        // 2) 기간 클램프 및 유효성 검사
        ClampedPeriod clamped = PeriodGuard.clamp(signup, period, today);

        // 3) 요청 기간 및 유효 기간 생성
        PeriodResponse requestedPeriod = PeriodResponse.of(period.getStart(), period.getEnd());
        PeriodResponse effectivePeriod = PeriodResponse.of(clamped.getStart(), clamped.getEnd());

        if (clamped.invalid()) {
            log.warn("[서비스] 월간 미룸 비율 조회 - 유효하지 않은 기간 요청 - userId: {}, period: {}", user.getId(), period);
            return PostponeRatioMonthResponse.empty(requestedPeriod, effectivePeriod);
        }

        // 4) 현재 및 이전 기간 요약 조회
        PostponedRatioSummary currentSummary = fetchSummary(user.getId(), clamped.getStart(), clamped.getEnd());
        PostponedRatioSummary previousSummary = fetchPreviousSummary(user.getId(), signup, (MonthPeriod) period.prev(), today);

        return PostponeRatioMonthResponse.of(
                requestedPeriod,
                effectivePeriod,
                currentSummary,
                previousSummary
        );
    }

    public PostponeRatioYearResponse getPostponeRatioYear(User user, YearPeriod period) {
        // 1) 회원 가입일 및 오늘 날짜 조회
        LocalDate signup = user.getCreatedAt().toLocalDate();
        LocalDate today = LocalDate.now();

        // 2) 기간 클램프 및 유효성 검사
        ClampedPeriod clamped = PeriodGuard.clamp(signup, period, today);

        // 3) 요청 기간 및 유효 기간 생성
        PeriodResponse requestedPeriod = PeriodResponse.of(period.getStart(), period.getEnd());
        PeriodResponse effectivePeriod = PeriodResponse.of(clamped.getStart(), clamped.getEnd());

        if (clamped.invalid()) {
            log.warn("[서비스] 연간 미룸 비율 조회 - 유효하지 않은 기간 요청 - userId: {}, period: {}", user.getId(), period);
            return PostponeRatioYearResponse.empty(requestedPeriod, effectivePeriod);
        }

        // 4) 기준 연도 월별 요약 조회
        int year = period.getStart().getYear();
        List<PostponedRatioSummary> monthlySummaries = java.util.stream.IntStream.rangeClosed(1, 12)
                .mapToObj(month -> fetchMonthlySummaryInRange(user.getId(), year, month, clamped))
                .toList();

        // 5) 이전 연도 요약 조회
        PostponedRatioSummary previousYearSummary = fetchPreviousSummary(user.getId(), signup, (YearPeriod) period.prev(), today);


        return PostponeRatioYearResponse.of(
                requestedPeriod,
                effectivePeriod,
                monthlySummaries,
                previousYearSummary
        );
    }

    /**
     * 현재 기간의 미룸 비율 요약 조회
     * - DB 조회 후 기록이 없으면 빈 요약(0%) 반환
     * - 기록이 있으면 정상 요약 생성
     */
    private PostponedRatioSummary fetchSummary(Long userId, LocalDate startDate, LocalDate endDate) {
        var counts = dailyLogRepository.getPostponeRatioCounts(userId, startDate, endDate);

        if (counts.getTotal() == 0L) {
            return PostponedRatioSummary.empty();
        }
        return PostponedRatioSummary.from(counts);
    }

    /**
     * 이전 기간(prev)의 미룸 비율 요약 조회
     * - 기간이 유효하지 않으면(가입 이전) null 반환
     * - 기록이 전혀 없으면 빈 요약 반환
     * - 기록이 있으면 요약 생성
     */
    private <T extends Period> PostponedRatioSummary fetchPreviousSummary(
            Long userId, LocalDate signup, T prevPeriod, LocalDate today) {
        ClampedPeriod prevClamped = PeriodGuard.clamp(signup, prevPeriod.getStart(), prevPeriod.getEnd(), today);

        // 이전 기간이 회원가입 이전임
        if (prevClamped.invalid()) {
            return null;
        }

        var prevCounts = dailyLogRepository.getPostponeRatioCounts(
                userId, prevClamped.getStart(), prevClamped.getEnd()
        );

        if (prevCounts.getTotal() == 0L) {
            return PostponedRatioSummary.empty();
        }

        return PostponedRatioSummary.from(prevCounts);
    }

    /**
     * 연도 범위 내 특정 월의 미룸 비율 요약 조회
     * - 월이 연도 범위를 벗어나면 빈 요약 반환
     * - 월이 범위와 겹치면 해당 구간만 조회
     */
    private PostponedRatioSummary fetchMonthlySummaryInRange(Long userId, int year, int month, ClampedPeriod yearRange) {
        // 1) 월 기간 생성
        MonthPeriod monthPeriod = MonthPeriod.of(year, month);

        // 2) 월 기간이 회원 가입 연도를 벗어나거나 미래인 경우 빈 요약 반환
        if (monthPeriod.getEnd().isBefore(yearRange.getStart()) ||
            monthPeriod.getStart().isAfter(yearRange.getEnd())) {
            return PostponedRatioSummary.empty();
        }

        // 3) 월 기간이 연도 범위와 겹치는 구간 계산
        LocalDate start = monthPeriod.getStart().isBefore(yearRange.getStart())
            ? yearRange.getStart() : monthPeriod.getStart();
        LocalDate end = monthPeriod.getEnd().isAfter(yearRange.getEnd())
            ? yearRange.getEnd() : monthPeriod.getEnd();

        return fetchSummary(userId, start, end);
    }
}
