package com.likelion.fourthlinethon.team1.cooltime.stats.dto.response;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PostponeRatioMonthResponse{

    /** 월 키 (연/월) */
    private MonthKey monthKey;

    /** 요청 기간 */
    private PeriodResponse requestedPeriod;

    /** 실제 유효 기간 */
    private PeriodResponse effectivePeriod;

    /** 상태  */
    private String status;

    /** 전월 대비 변화 정보 */
    private PostponedRateChangeResponse change;

    /** 미룸 비율 정보 */
    private PostponedRatioSummary ratio;

    public static PostponeRatioMonthResponse of(
            PeriodResponse requestedPeriod,
            PeriodResponse effectivePeriod,
            PostponedRatioSummary current,
            @Nullable PostponedRatioSummary previous
    ){
        MonthKey key = MonthKey.from(requestedPeriod.getStartDate());
        PostponedRateChangeResponse change = PostponedRateChangeResponse.of(
                current.getPostponedPercent(),
                previous != null ? previous.getPostponedPercent() : null
        );

        return PostponeRatioMonthResponse.builder()
                .monthKey(key)
                .requestedPeriod(requestedPeriod)
                .effectivePeriod(effectivePeriod)
                .status(EffectivePeriodStatus.OK.name())
                .change(change)
                .ratio(current)
                .build();
    }

    public static PostponeRatioMonthResponse empty(
            PeriodResponse requestedPeriod,
            PeriodResponse effectivePeriod
    ){
        MonthKey key = MonthKey.from(requestedPeriod.getStartDate());

        return PostponeRatioMonthResponse.builder()
                .monthKey(key)
                .requestedPeriod(requestedPeriod)
                .effectivePeriod(effectivePeriod)
                .status(EffectivePeriodStatus.OUT_OF_RANGE.name())
                .change(PostponedRateChangeResponse.noData())
                .ratio(PostponedRatioSummary.empty())
                .build();
    }
}