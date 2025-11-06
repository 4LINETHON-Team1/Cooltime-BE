package com.likelion.fourthlinethon.team1.cooltime.stats.dto.response;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostponeRatioYearResponse {

    /** 연도 */
    private int year;

    /** 기간  */
    private PeriodResponse period;

    /** 전년도 대비 변화 정보 */
    private PostponedRateChangeResponse change;

    /** 월별 포인트 리스트 */
    private List<MonthlyPoint> months;

    public static PostponeRatioYearResponse of(
            LocalDate startDate,
            LocalDate endDate,
            List<PostponedRatioSummary> monthlySummaries,
            @Nullable PostponedRatioSummary previousYearSummary
    ) {
        int year = startDate.getYear();

        // 1) 월별 포인트 생성 (표시 용)
        List<MonthlyPoint> monthPoints = new ArrayList<>();
        for (int i = 0; i < monthlySummaries.size(); i++) {
            int month = i + 1;
            PostponedRatioSummary s = monthlySummaries.get(i);
            MonthKey key = MonthKey.of(year, month);
            monthPoints.add(MonthlyPoint.of(key, s.getPostponedPercent()));
        }
        // 2) 기간
        PeriodResponse period = PeriodResponse.of(startDate, endDate);

        // 3) 연간 퍼센트는 누적합 기반으로 계산
        int currentYearPercent = computePercentFromSummaries(monthlySummaries);

        // 4) 전년도 대비 변화 (이미 계산된 요약에서 퍼센트만 꺼냄)
        Integer previousYearPercent = (previousYearSummary == null) ? null : previousYearSummary.getPostponedPercent();
        PostponedRateChangeResponse change = PostponedRateChangeResponse.of(currentYearPercent, previousYearPercent);

        // 5) 조립
        return PostponeRatioYearResponse.builder()
                .year(year)
                .period(period)
                .change(change)
                .months(monthPoints)
                .build();
    }

    /** 누적합 기반으로 퍼센트 계산: round( sum(postponed) / (sum(postponed)+sum(done)) * 100 ) */
    private static int computePercentFromSummaries(List<PostponedRatioSummary> list) {
        if (list == null || list.isEmpty()) return 0;

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
        private MonthKey monthKey;
        private int postponedPercent;

        public static MonthlyPoint of(MonthKey key, int postponedPercent) {
            return MonthlyPoint.builder()
                    .monthKey(key)
                    .postponedPercent(postponedPercent)
                    .build();
        }

    }
}