package com.likelion.fourthlinethon.team1.cooltime.stats.dto.response;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class PostponeRatioYearResponse {

    /** 연도 */
    private int year;

    /** 요청 기간 */
    private PeriodResponse requestedPeriod;

    /** 실제 유효 기간 */
    private PeriodResponse effectivePeriod;

    /** 상태  */
    private String status;

    /** 전년도 대비 변화 정보 */
    private PostponedRateChangeResponse change;

    /** 월별 포인트 리스트 */
    private List<MonthlyPoint> months;

    public static PostponeRatioYearResponse of(
            PeriodResponse requestedPeriod,
            PeriodResponse effectivePeriod,
            List<PostponedRatioSummary> monthlySummaries,
            @Nullable PostponedRatioSummary previousYearSummary
    ) {
        int year = requestedPeriod.getStartDate().getYear();

        // 1) 월별 포인트 생성 (표시 용)
        List<MonthlyPoint> monthPoints = new ArrayList<>();
        for (int i = 0; i < monthlySummaries.size(); i++) {
            int month = i + 1;
            PostponedRatioSummary s = monthlySummaries.get(i);
            monthPoints.add(MonthlyPoint.of(month, s.getPostponedPercent()));
        }

        // 2) 연간 퍼센트는 누적합 기반으로 계산
        int currentYearPercent = computePercentFromSummaries(monthlySummaries);

        // 3) 전년도 대비 변화 (이미 계산된 요약에서 퍼센트만 꺼냄)
        Integer previousYearPercent = (previousYearSummary == null) ? null : previousYearSummary.getPostponedPercent();
        PostponedRateChangeResponse change = PostponedRateChangeResponse.of(currentYearPercent, previousYearPercent);

        // 4) 조립
        return PostponeRatioYearResponse.builder()
                .year(year)
                .requestedPeriod(requestedPeriod)
                .effectivePeriod(effectivePeriod)
                .status(EffectivePeriodStatus.OK.name())
                .change(change)
                .months(monthPoints)
                .build();
    }

    public static  PostponeRatioYearResponse empty(
            PeriodResponse requestedPeriod,
            PeriodResponse effectivePeriod
    ) {
        int year = requestedPeriod.getStartDate().getYear();

        // 1~12월까지 postponedPercent=0인 MonthlyPoint 생성
        List<MonthlyPoint> emptyMonths = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            emptyMonths.add(MonthlyPoint.of(month, 0));
        }

        return PostponeRatioYearResponse.builder()
                .year(year)
                .requestedPeriod(requestedPeriod)
                .effectivePeriod(effectivePeriod)
                .status(EffectivePeriodStatus.OUT_OF_RANGE.name())
                .months(emptyMonths)
                .build();
    }

    /** 누적합 기반으로 퍼센트 계산: round( sum(postponed) / (sum(postponed)+sum(done)) * 100 ) */
    private static int computePercentFromSummaries(List<PostponedRatioSummary> list) {
        if (list == null || list.isEmpty()) {
            log.warn("[PostponeRatioYearResponse] computePercentFromSummaries: empty list");
            return 0;
        }

        long sumPostponed = 0;
        long sumDone = 0;

        for (PostponedRatioSummary s : list) {
            if (s == null) continue;
            sumPostponed += Math.max(0, s.getPostponedCount());
            sumDone += Math.max(0, s.getDoneCount());
        }
        long total = sumPostponed + sumDone;
        if (total == 0) return 0;

        return (int) Math.round((double) sumPostponed / total * 100);
    }

        /** 내부 월별 포인트 클래스 */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MonthlyPoint {
        private int month;
        private int postponedPercent;

        public static MonthlyPoint of(int month, int postponedPercent) {
            return MonthlyPoint.builder()
                    .month(month)
                    .postponedPercent(postponedPercent)
                    .build();
        }

    }
}